package tech.ccat.kstats.model

class EntityStat(
    health: Double = 20.0,
    defense: Double = 0.0,
    strength: Double = 0.0,
    speed: Double = 100.0,
    baseDamage: Double = 1.0
) : BaseEntityStat(health, defense, strength, speed, baseDamage) {

    override fun copy(): EntityStat = EntityStat(health, defense, strength, speed, baseDamage)
}
