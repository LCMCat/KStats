package tech.ccat.kstats.listener

import org.bukkit.entity.Entity
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
        val api = KStats.instance

        val attacker: LivingEntity = when (event.damager) {
            is Projectile -> (event.damager as Projectile).shooter as? LivingEntity ?: return
            is LivingEntity -> event.damager as LivingEntity
            else -> return
        }
        val defender = event.entity as? LivingEntity ?: return

        val finalDamage = CombatEngine.calculateFinalDamage(attacker, defender, event.damage)

        if (defender is Player) {
            event.damage = 0.0

            val killer: Entity = when (event.damager) {
                is Projectile -> event.damager as Projectile
                else -> attacker
            }
            api.damagePlayer(defender, finalDamage, event.cause, killer)
        } else {
            event.damage = finalDamage
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onGenericDamage(event: EntityDamageEvent) {
        if (event is EntityDamageByEntityEvent) return

        val player = event.entity as? Player ?: return

        val api = KStats.instance

        val finalDamage = when (event.cause) {
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.FLY_INTO_WALL -> {
                val defenderStats = api.getAllStats(player)
                val defense = defenderStats.defense
                event.damage * (1 - defense / (defense + 100))
            }
            else -> event.damage
        }

        event.damage = 0.0
        api.damagePlayer(player, finalDamage, event.cause, null)
    }
}
