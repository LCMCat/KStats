package tech.ccat.kstats.service

import org.bukkit.entity.Player
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.StatType
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class HealthManager {
    private val realHealthCache = ConcurrentHashMap<UUID, Double>()

    companion object {
        const val DISPLAY_MAX_HEALTH = 20.0
    }

    fun getMaxHealth(player: Player): Double {
        return KStats.instance.getStat(player, StatType.HEALTH)
    }

    fun getRealHealth(player: Player): Double {
        val maxHealth = getMaxHealth(player)
        return realHealthCache.getOrDefault(player.uniqueId, maxHealth)
    }

    fun setRealHealth(player: Player, health: Double) {
        val maxHealth = getMaxHealth(player)
        val clamped = health.coerceIn(0.0, maxHealth)
        realHealthCache[player.uniqueId] = clamped
        syncDisplayToPlayer(player)
    }

    fun damage(player: Player, amount: Double) {
        val current = getRealHealth(player)
        setRealHealth(player, current - amount)
    }

    fun heal(player: Player, amount: Double) {
        val current = getRealHealth(player)
        setRealHealth(player, current + amount)
    }

    fun toDisplayHealth(realHealth: Double, maxHealth: Double): Double {
        if (maxHealth <= 0) return DISPLAY_MAX_HEALTH
        return (realHealth / maxHealth) * DISPLAY_MAX_HEALTH
    }

    fun toRealHealth(displayHealth: Double, maxHealth: Double): Double {
        return (displayHealth / DISPLAY_MAX_HEALTH) * maxHealth
    }

    private fun syncDisplayToPlayer(player: Player) {
        val maxHealth = getMaxHealth(player)
        val realHealth = getRealHealth(player)
        val displayHealth = toDisplayHealth(realHealth, maxHealth)
        player.health = displayHealth.coerceIn(0.5, DISPLAY_MAX_HEALTH)
    }

    fun syncDisplay(player: Player) {
        val uuid = player.uniqueId
        val maxHealth = getMaxHealth(player)
        val displayHealth = player.health

        val isFullHealth = displayHealth >= DISPLAY_MAX_HEALTH

        val realHealth = if (isFullHealth) {
            maxHealth
        } else {
            realHealthCache.getOrDefault(uuid, toRealHealth(displayHealth, maxHealth))
        }

        realHealthCache[uuid] = realHealth.coerceIn(0.0, maxHealth)

        val newDisplayHealth = toDisplayHealth(realHealth, maxHealth)
        player.health = newDisplayHealth.coerceIn(0.5, DISPLAY_MAX_HEALTH)
    }

    fun initFromDisplay(player: Player) {
        val maxHealth = getMaxHealth(player)
        val savedHealth = player.health

        val realHealth = if (savedHealth > DISPLAY_MAX_HEALTH) {
            savedHealth.coerceIn(0.0, maxHealth)
        } else {
            toRealHealth(savedHealth, maxHealth)
        }

        realHealthCache[player.uniqueId] = realHealth.coerceIn(0.0, maxHealth)
    }

    fun removePlayer(uuid: UUID) {
        realHealthCache.remove(uuid)
    }

    fun clear() {
        realHealthCache.clear()
    }
}
