package tech.ccat.kstats.util

import org.bukkit.Bukkit
import tech.ccat.kstats.KStats

object SchedulerUtil {
    fun runAsync(task: Runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(KStats.instance, task)
    }
}