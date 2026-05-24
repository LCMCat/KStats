package tech.ccat.kstats.task

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tech.ccat.kstats.KStats

class HealingTask(private val player: Player) : BukkitRunnable() {
    private val plugin = KStats.instance

    fun start() {
        runTaskTimer(plugin, 40L, 40L)
    }

    override fun run() {
        if (!player.isOnline || player.isDead) return

        val stats = plugin.statManager.getAllStats(player)
        val maxHealth = stats.health
        val healingAmount = (maxHealth / 100 + 1.5) * (stats.healing / 100)

        Bukkit.getScheduler().runTask(plugin, Runnable {
            if (player.isOnline && !player.isDead) {
                plugin.cacheService.heal(player, healingAmount)
            }
        })
    }
}
