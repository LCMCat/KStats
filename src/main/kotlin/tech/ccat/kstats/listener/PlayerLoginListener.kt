package tech.ccat.kstats.listener

import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.service.CacheService

class PlayerLoginListener : Listener {
    private val plugin = KStats.instance

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.statManager.initPlayerStats(player)

        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = CacheService.DISPLAY_MAX_HEALTH

        plugin.statManager.updateStats(player)

        plugin.cacheService.initFromDisplay(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.cacheService.removePlayer(event.player.uniqueId)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        plugin.cacheService.heal(player)
    }
}
