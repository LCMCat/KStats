package tech.ccat.kstats.event

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent

class PlayerSetDeathEvent(
    val player: Player,
    val deathLocation: Location,
    val cause: EntityDamageEvent.DamageCause?,
    val killer: Entity? = null
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
