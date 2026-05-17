package tech.ccat.kstats.service

import org.bukkit.entity.LivingEntity
import tech.ccat.kstats.api.EntityStatProvider
import tech.ccat.kstats.model.EntityStat
import tech.ccat.kstats.model.StatType

class EntityStatManager {
    private var provider: EntityStatProvider? = null

    fun getStat(entity: LivingEntity, statType: StatType): Double {
        if (!statType.isBaseStat()) return 0.0
        val p = provider
        if (p != null) {
            return p.provideStats(entity).getStatValue(statType)
        }
        return when (statType) {
            StatType.HEALTH -> 20.0
            StatType.SPEED -> 100.0
            StatType.BASE_DAMAGE -> 1.0
            else -> 0.0
        }
    }

    fun getAllStats(entity: LivingEntity): EntityStat {
        val p = provider
        if (p != null) {
            return p.provideStats(entity)
        }
        return EntityStat()
    }

    fun setProvider(provider: EntityStatProvider) {
        this.provider = provider
    }

    fun clearProvider() {
        this.provider = null
    }

    fun getProvider(): EntityStatProvider? = provider
}
