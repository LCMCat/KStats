package tech.ccat.kstats.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.kstats.task.HealingTask
import tech.ccat.kstats.task.ManaRegenTask
import java.util.UUID

class HealingListener : Listener {
    private val healingTasks = hashMapOf<UUID, HealingTask>()
    private val manaRegenTasks = hashMapOf<UUID, ManaRegenTask>()

    internal fun addPlayerToTask(player: Player){
        healingTasks[player.uniqueId] = HealingTask(player).apply {
            start()
        }
        manaRegenTasks[player.uniqueId] = ManaRegenTask(player).apply {
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
        val uuid = event.player.uniqueId
        healingTasks.remove(uuid)?.cancel()
        manaRegenTasks.remove(uuid)?.cancel()
    }
}
