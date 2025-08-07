package tech.ccat.kstats.listener

import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import tech.ccat.kstats.event.StatUpdateEvent

class StatUpdateListener : Listener {
    @EventHandler
    fun onStatUpdate(event: StatUpdateEvent) {
        val player = event.player
        val stats = event.stats

        // 应用生命值
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = stats.health
    }
}