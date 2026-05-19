package tech.ccat.kstats.listener

import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.service.HealthManager

class PlayerLoginListener : Listener {
    private val plugin = KStats.instance

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.statManager.initPlayerStats(player)

        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = HealthManager.DISPLAY_MAX_HEALTH

        plugin.healthManager.initFromDisplay(player)

        plugin.statManager.updateStats(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.healthManager.removePlayer(event.player.uniqueId)
        plugin.cacheService.removePlayer(event.player.uniqueId)
    }
}
