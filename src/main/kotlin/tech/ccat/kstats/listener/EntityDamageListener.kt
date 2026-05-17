package tech.ccat.kstats.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.util.CombatEngine

class EntityDamageListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.entity !is LivingEntity) return

        when (event.damager) {
            is LivingEntity -> {}
            is Projectile -> {
                val shooter = (event.damager as Projectile).shooter
                if (shooter !is LivingEntity) return
            }
            else -> return
        }

        CombatEngine.handleDamage(event)
    }
}
