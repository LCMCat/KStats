package tech.ccat.kstats.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.KStats

class BaseDamageCaptureListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val attacker: LivingEntity = when (event.damager) {
            is Projectile -> (event.damager as Projectile).shooter as? LivingEntity ?: return
            is LivingEntity -> event.damager as LivingEntity
            else -> return
        }
        if (event.entity !is LivingEntity) return

        val attackerStats = KStats.instance.getAllStats(attacker)

        event.damage = attackerStats.baseDamage
    }
}
