package tech.ccat.kstats.listener

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.event.PlayerSetDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerSetDeathEvent) {
        val player = event.player
        val api = KStats.instance

        val respawnLocation = player.respawnLocation ?: player.world.spawnLocation
        player.teleport(respawnLocation)

        api.healPlayer(player)

        val deathMessage = buildDeathMessage(event)
        Bukkit.broadcastMessage(deathMessage)
    }

    private fun buildDeathMessage(event: PlayerSetDeathEvent): String {
        val playerName = event.player.name
        val cause = event.cause
        val killer = event.killer

        val killerName = when (killer) {
            is Player -> killer.name
            is Entity -> killer.name ?: "未知"
            else -> null
        }

        val messageKey = getMessageKey(cause, killerName != null)
        return KStats.instance.configManager.messageConfig.getDeathMessage(messageKey, playerName, killerName)
    }

    private fun getMessageKey(cause: EntityDamageEvent.DamageCause?, hasKiller: Boolean): String {
        val baseKey = when (cause) {
            EntityDamageEvent.DamageCause.VOID -> "death.void"
            EntityDamageEvent.DamageCause.FALL -> "death.fall"
            EntityDamageEvent.DamageCause.FLY_INTO_WALL -> "death.fly-into-wall"
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA -> "death.fire"
            EntityDamageEvent.DamageCause.DROWNING -> "death.drowning"
            EntityDamageEvent.DamageCause.SUFFOCATION -> "death.suffocation"
            EntityDamageEvent.DamageCause.CONTACT -> "death.contact"
            EntityDamageEvent.DamageCause.MAGIC -> "death.magic"
            EntityDamageEvent.DamageCause.WITHER -> "death.wither"
            EntityDamageEvent.DamageCause.POISON -> "death.poison"
            EntityDamageEvent.DamageCause.STARVATION -> "death.starvation"
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK -> "death.entity-attack"
            EntityDamageEvent.DamageCause.PROJECTILE -> "death.projectile"
            EntityDamageEvent.DamageCause.THORNS -> "death.thorns"
            EntityDamageEvent.DamageCause.HOT_FLOOR -> "death.hot-floor"
            EntityDamageEvent.DamageCause.CRAMMING -> "death.cramming"
            EntityDamageEvent.DamageCause.DRYOUT -> "death.dryout"
            EntityDamageEvent.DamageCause.FREEZE -> "death.freeze"
            EntityDamageEvent.DamageCause.KILL -> "death.kill"
            else -> "death.unknown"
        }

        return if (hasKiller && hasKillerVariant(cause)) {
            "$baseKey-by"
        } else {
            baseKey
        }
    }

    private fun hasKillerVariant(cause: EntityDamageEvent.DamageCause?): Boolean {
        return cause in setOf(
            EntityDamageEvent.DamageCause.VOID,
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.PROJECTILE,
            EntityDamageEvent.DamageCause.THORNS,
            EntityDamageEvent.DamageCause.KILL
        )
    }
}
