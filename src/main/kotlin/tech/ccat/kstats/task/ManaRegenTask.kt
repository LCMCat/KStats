package tech.ccat.kstats.task

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tech.ccat.kstats.KStats

class ManaRegenTask(private val player: Player) : BukkitRunnable() {
    private val plugin = KStats.instance

    fun start() {
        runTaskTimer(plugin, 20L, 20L)
    }

    override fun run() {
        if (!player.isOnline) return

        val stats = plugin.statManager.getAllStats(player)
        val maxMana = stats.wisdom
        val manaRegen = stats.manaRegen
        val currentMana = plugin.cacheService.getMana(player.uniqueId)

        if (currentMana >= maxMana) return

        val regenAmount = maxMana * 0.02 * (1 + manaRegen / 100)
        val newMana = (currentMana + regenAmount).coerceAtMost(maxMana)

        plugin.cacheService.setMana(player.uniqueId, newMana)
    }
}
