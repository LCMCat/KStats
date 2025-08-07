package tech.ccat.kstats.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import tech.ccat.kstats.model.PlayerStat

class StatUpdateEvent(
    val player: Player,
    val stats: PlayerStat
) : Event() {
    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlers
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}