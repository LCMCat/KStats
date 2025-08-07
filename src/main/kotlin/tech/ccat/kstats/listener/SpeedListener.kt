package tech.ccat.kstats.listener

import tech.ccat.kstats.event.StatUpdateEvent

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class SpeedListener : Listener {
    @EventHandler
    fun onStatUpdate(event: StatUpdateEvent) {
        val player = event.player
        val speed = event.stats.speed
        player.walkSpeed = (0.2 * (speed / 100.0)).toFloat()
    }
}