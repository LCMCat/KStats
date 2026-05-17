package tech.ccat.kstats.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ManaConsumptionEvent(
    val player: Player,
    val amount: Double,
    val reason: String
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
