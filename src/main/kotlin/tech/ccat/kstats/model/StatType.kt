package tech.ccat.kstats.model

enum class StatType {
    HEALTH,
    DEFENSE,
    STRENGTH,
    SPEED,
    CRIT_CHANCE,
    CRIT_DAMAGE,
    WISDOM,
    BASE_DAMAGE,
    DAMAGE_MULTIPLIER,
    HEALING,
    MANA_REGEN;

    companion object {
        val BASE_STATS: Set<StatType> = setOf(HEALTH, DEFENSE, STRENGTH, SPEED, BASE_DAMAGE)
        val PLAYER_ONLY_STATS: Set<StatType> = setOf(CRIT_CHANCE, CRIT_DAMAGE, WISDOM, DAMAGE_MULTIPLIER, HEALING, MANA_REGEN)
    }

    fun isBaseStat(): Boolean = this in BASE_STATS

    fun isPlayerOnly(): Boolean = this in PLAYER_ONLY_STATS
}
