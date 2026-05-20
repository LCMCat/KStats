package tech.ccat.kstats.api

import org.bukkit.entity.LivingEntity
import tech.ccat.kstats.model.EntityStat

/**
 * 实体属性提供者接口，用于为非玩家实体提供属性数据
 *
 * 实现此接口的类应：
 * 1. 通过KStatsAPI.setEntityProvider()注册
 * 2. 在插件禁用时通过KStatsAPI.clearEntityProvider()清除
 *
 * 示例实现：
 * ```kotlin
 * class MyEntityStatProvider : EntityStatProvider {
 *     override fun provideStats(entity: LivingEntity): EntityStat {
 *         return EntityStat().apply {
 *             health = 50.0
 *             defense = 10.0
 *         }
 *     }
 * }
 * ```
 */
interface EntityStatProvider {

    /**
     * 为指定实体生成属性数据
     *
     * @param entity 目标实体
     * @return EntityStat对象包含此提供者生成的属性数据
     */
    fun provideStats(entity: LivingEntity): EntityStat
}
