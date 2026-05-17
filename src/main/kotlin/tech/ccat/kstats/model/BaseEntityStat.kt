package tech.ccat.kstats.model

open class BaseEntityStat(
    var health: Double = 20.0,
    var defense: Double = 0.0,
    var strength: Double = 0.0,
    var speed: Double = 100.0,
    var baseDamage: Double = 1.0
) {
    open fun getStatValue(statType: StatType): Double = when (statType) {
        StatType.HEALTH -> health
        StatType.DEFENSE -> defense
        StatType.STRENGTH -> strength
        StatType.SPEED -> speed
        StatType.BASE_DAMAGE -> baseDamage
        else -> throw IllegalArgumentException("StatType $statType is not a base stat")
    }

    open fun addAllStats(other: BaseEntityStat) {
        health += other.health
        defense += other.defense
        strength += other.strength
        speed += other.speed
        baseDamage += other.baseDamage
    }

    open fun copy(): BaseEntityStat = BaseEntityStat(health, defense, strength, speed, baseDamage)
}
