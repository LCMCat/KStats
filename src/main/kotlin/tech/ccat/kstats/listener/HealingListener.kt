package tech.ccat.kstats.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.kstats.task.HealingTask
import java.util.UUID

class HealingListener : Listener {
    private val tasks = hashMapOf<UUID, HealingTask>()

    internal fun addPlayerToTask(player: Player){
        tasks[player.uniqueId] = HealingTask(player).apply {
            start()
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        addPlayerToTask(player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        tasks.remove(event.player.uniqueId)?.cancel()
    }
}