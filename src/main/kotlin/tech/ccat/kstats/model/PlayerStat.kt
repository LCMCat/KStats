package tech.ccat.kstats.model

class PlayerStat(
    health: Double = 0.0,
    defense: Double = 0.0,
    strength: Double = 0.0,
    speed: Double = 0.0,
    baseDamage: Double = 0.0,
    var critChance: Double = 0.0,
    var critDamage: Double = 0.0,
    var wisdom: Double = 0.0,
    var damageMultiplier: Double = 0.0,
    var healing: Double = 0.0,
    var manaRegen: Double = 0.0
) : BaseEntityStat(health, defense, strength, speed, baseDamage) {

    override fun getStatValue(statType: StatType): Double = when (statType) {
        StatType.CRIT_CHANCE -> critChance
        StatType.CRIT_DAMAGE -> critDamage
        StatType.WISDOM -> wisdom
        StatType.DAMAGE_MULTIPLIER -> damageMultiplier
        StatType.HEALING -> healing
        StatType.MANA_REGEN -> manaRegen
        else -> super.getStatValue(statType)
    }

    override fun addAllStats(other: BaseEntityStat) {
        super.addAllStats(other)
        if (other is PlayerStat) {
            critChance += other.critChance
            critDamage += other.critDamage
            wisdom += other.wisdom
            damageMultiplier += other.damageMultiplier
            healing += other.healing
            manaRegen += other.manaRegen
        }
    }

    override fun copy(): PlayerStat = PlayerStat(
        health, defense, strength, speed, baseDamage,
        critChance, critDamage, wisdom, damageMultiplier, healing, manaRegen
    )
}
