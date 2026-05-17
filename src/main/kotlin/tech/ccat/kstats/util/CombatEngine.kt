package tech.ccat.kstats.util

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.KStats

object CombatEngine {
    fun handleDamage(event: EntityDamageByEntityEvent) {
        val attacker: LivingEntity = when (event.damager) {
            is Projectile -> (event.damager as Projectile).shooter as? LivingEntity ?: return
            is LivingEntity -> event.damager as LivingEntity
            else -> return
        }
        val defender = event.entity as? LivingEntity ?: return

        val attackerStats = KStats.instance.getAllStats(attacker)
        val defenderStats = KStats.instance.getAllStats(defender)

        val damage = attackerStats.baseDamage * (1 + attackerStats.strength / 100)

        val defense = defenderStats.defense
        val finalDamage = damage * (1 - defense / (defense + 100))

        event.damage = finalDamage
    }
}
