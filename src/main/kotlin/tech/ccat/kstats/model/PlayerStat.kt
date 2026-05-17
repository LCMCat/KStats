package tech.ccat.kstats.model

import org.bukkit.configuration.serialization.ConfigurationSerializable

data class PlayerStat(
    var health: Double = 0.0,
    var defense: Double = 0.0,
    var strength: Double = 0.0,
    var speed: Double = 0.0,
    var critChance: Double = 30.0,
    var critDamage: Double = 50.0,
    var wisdom: Double = 100.0,
    var baseDamage: Double = 1.0,
    var damageMultiplier: Double = 1.0,
    var healing: Double = 100.0,
    var manaRegen: Double = 0.0
) : ConfigurationSerializable {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>) = PlayerStat(
            health = map["health"] as? Double ?: 20.0,
            defense = map["defense"] as? Double ?: 0.0,
            strength = map["strength"] as? Double ?: 0.0,
            speed = map["speed"] as? Double ?: 0.0,
            critChance = map["critChance"] as? Double ?: 30.0,
            critDamage = map["critDamage"] as? Double ?: 50.0,
            wisdom = map["wisdom"] as? Double ?: 100.0,
            baseDamage = map["baseDamage"] as? Double ?: 1.0,
            damageMultiplier = map["damageMultiplier"] as? Double ?: 1.0,
            healing = map["healing"] as? Double ?: 100.0,
            manaRegen = map["manaRegen"] as? Double ?: 0.0
        )
    }

    override fun serialize() = linkedMapOf<String, Any>(
        "health" to health,
        "defense" to defense,
        "strength" to strength,
        "speed" to speed,
        "critChance" to critChance,
        "critDamage" to critDamage,
        "wisdom" to wisdom,
        "baseDamage" to baseDamage,
        "damageMultiplier" to damageMultiplier,
        "healing" to healing,
        "manaRegen" to manaRegen
    )

    fun getStatValue(type: StatType): Double = when (type) {
        StatType.HEALTH -> health
        StatType.DEFENSE -> defense
        StatType.STRENGTH -> strength
        StatType.SPEED -> speed
        StatType.CRIT_CHANCE -> critChance
        StatType.CRIT_DAMAGE -> critDamage
        StatType.WISDOM -> wisdom
        StatType.BASE_DAMAGE -> baseDamage
        StatType.DAMAGE_MULTIPLIER -> damageMultiplier
        StatType.HEALING -> healing
        StatType.MANA_REGEN -> manaRegen
    }

    fun addAllStats(other: PlayerStat) {
        health += other.health
        defense += other.defense
        strength += other.strength
        speed += other.speed
        critChance += other.critChance
        critDamage += other.critDamage
        wisdom += other.wisdom
        baseDamage += other.baseDamage
        damageMultiplier += other.damageMultiplier
        healing += other.healing
        manaRegen += other.manaRegen
    }
}
