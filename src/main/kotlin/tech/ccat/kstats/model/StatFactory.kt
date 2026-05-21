package tech.ccat.kstats.model

object StatFactory {

    @JvmStatic
    fun emptyPlayerStat(): PlayerStat = PlayerStat(
        health = 0.0,
        defense = 0.0,
        strength = 0.0,
        speed = 0.0,
        baseDamage = 0.0,
        critChance = 0.0,
        critDamage = 0.0,
        wisdom = 0.0,
        damageMultiplier = 0.0,
        healing = 0.0,
        manaRegen = 0.0
    )

    @JvmStatic
    fun emptyBaseEntityStat(): BaseEntityStat = BaseEntityStat(
        health = 0.0,
        defense = 0.0,
        strength = 0.0,
        speed = 0.0,
        baseDamage = 0.0
    )
}

