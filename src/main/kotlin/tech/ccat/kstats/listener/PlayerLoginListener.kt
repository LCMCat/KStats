package tech.ccat.kstats.listener

import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.StatType

class PlayerLoginListener : Listener {
    private val plugin = KStats.instance

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // 初始化并更新属性
        val player = event.player
        plugin.statManager.initPlayerStats(player)
        plugin.statManager.updateStats(player)
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = plugin.statManager.getStat(player, StatType.HEALTH)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // 清理缓存
        plugin.cacheService.removePlayer(event.player.uniqueId)
    }
}