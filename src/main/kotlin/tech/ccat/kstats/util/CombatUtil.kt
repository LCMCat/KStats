package tech.ccat.kstats.util

import kotlin.random.Random

object CombatUtil {
    fun calculateDamage(
        baseDamage: Double,
        strength: Double,
        critChance: Double,
        critDamage: Double,
        damageMultiplier: Double
    ): Double {
        val isCrit = Random.nextDouble(0.0, 100.0) <= critChance
        val critMultiplier = if (isCrit) (1 + critDamage / 100) else 1.0
        return baseDamage * (1 + strength / 100) * critMultiplier * damageMultiplier
    }

    fun isCritical(d: Double): Boolean {
        return Random(System.currentTimeMillis()).nextDouble() < ( d / 100 )
    }
}