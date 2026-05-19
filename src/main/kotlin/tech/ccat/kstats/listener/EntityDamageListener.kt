package tech.ccat.kstats.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.CombatEngine

class EntityDamageListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val attacker: LivingEntity = when (event.damager) {
            is Projectile -> (event.damager as Projectile).shooter as? LivingEntity ?: return
            is LivingEntity -> event.damager as LivingEntity
            else -> return
        }
        val defender = event.entity as? LivingEntity ?: return

        val finalDamage = CombatEngine.calculateFinalDamage(attacker, defender, event.damage)

        if (defender is Player) {
            val plugin = KStats.instance
            val maxHealth = plugin.healthManager.getMaxHealth(defender)
            val displayDamage = plugin.healthManager.toDisplayHealth(finalDamage, maxHealth)
            event.damage = displayDamage
        } else {
            event.damage = finalDamage
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamageMonitor(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        KStats.instance.healthManager.syncDisplay(player)
    }
}
