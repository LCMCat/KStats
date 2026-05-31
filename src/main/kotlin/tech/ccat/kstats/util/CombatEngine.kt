package tech.ccat.kstats.util

import org.bukkit.entity.LivingEntity
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.PlayerStat
import kotlin.random.Random

object CombatEngine {
    fun calculateFinalDamage(attacker: LivingEntity, defender: LivingEntity, baseDamage: Double): Double {
        val attackerStats = KStats.instance.getAllStats(attacker)
        val defenderStats = KStats.instance.getAllStats(defender)

        var damage = baseDamage * (1 + attackerStats.strength / 100)

        if (attackerStats is PlayerStat) {
            damage *= attackerStats.damageMultiplier
        }

        val defense = defenderStats.defense
        damage *= (1 - defense / (defense + 100))

        return damage
    }

    fun isCritical(critChance: Double): Boolean {
        return Random(System.currentTimeMillis()).nextDouble() < (critChance / 100)
    }
}
