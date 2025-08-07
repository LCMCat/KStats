// tech/ccat/kstats/task/HealingTask.kt
package tech.ccat.kstats.task

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.SchedulerUtil

class HealingTask(private val player: Player) : BukkitRunnable() {
    private val plugin = KStats.instance

    fun start() {
        runTaskTimer(plugin, 40L, 40L) // 2秒间隔
    }

    override fun run() {
        if (!player.isOnline) return

        SchedulerUtil.runAsync {
            val stats = plugin.statManager.getAllStats(player)
            val healingAmount = (stats.health/100 + 1.5) * (stats.healing/100)

            player.health = (player.health + healingAmount)
                .coerceAtMost(stats.health)
        }
    }
}