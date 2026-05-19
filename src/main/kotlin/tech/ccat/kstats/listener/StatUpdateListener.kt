package tech.ccat.kstats.listener

import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import tech.ccat.kstats.KStats
import tech.ccat.kstats.event.StatUpdateEvent
import tech.ccat.kstats.service.HealthManager

class StatUpdateListener : Listener {
    @EventHandler
    fun onStatUpdate(event: StatUpdateEvent) {
        val player = event.player
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = HealthManager.DISPLAY_MAX_HEALTH
        KStats.instance.healthManager.syncDisplay(player)
    }
}
