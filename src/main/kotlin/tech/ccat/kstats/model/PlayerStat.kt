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
    var healing: Double = 100.0
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
            healing = map["healing"] as? Double ?: 100.0
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
        "healing" to healing
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
    }

    fun addAllStats(other: PlayerStat) {
        health = this.health + other.health
        defense = this.defense + other.defense
        strength = this.strength + other.strength
        speed = this.speed + other.speed
        critChance = this.critChance + other.critChance
        critDamage = this.critDamage + other.critDamage
        wisdom = this.wisdom + other.wisdom
        baseDamage = this.baseDamage + other.baseDamage
        damageMultiplier = this.damageMultiplier + other.damageMultiplier
        healing = this.healing + other.healing
    }
}