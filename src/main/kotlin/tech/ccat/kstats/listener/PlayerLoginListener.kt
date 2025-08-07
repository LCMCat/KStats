package tech.ccat.kstats.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.kstats.KStats

class PlayerLoginListener : Listener {
    private val plugin = KStats.instance

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // 初始化并更新属性
        plugin.statManager.initPlayerStats(event.player)
        plugin.statManager.updateStats(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // 清理缓存
        plugin.cacheService.removePlayer(event.player.uniqueId)
    }
}