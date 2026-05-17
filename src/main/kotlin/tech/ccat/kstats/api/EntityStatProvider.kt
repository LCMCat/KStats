package tech.ccat.kstats.api

import org.bukkit.entity.LivingEntity
import tech.ccat.kstats.model.EntityStat

interface EntityStatProvider {
    fun provideStats(entity: LivingEntity): EntityStat
}
