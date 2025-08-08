package tech.ccat.kstats.api

import org.bukkit.entity.Player
import tech.ccat.kstats.model.PlayerStat

/**
 * 状态提供者接口，用于扩展玩家的状态值
 *
 * 实现此接口的类应：
 * 1. 在插件启用时注册到KStatsAPI
 * 2. 在插件禁用时取消注册
 *
 * 示例实现：
 * class MyStatProvider : StatProvider {
 *     override fun provideStats(player: Player): PlayerStat {
 *         return PlayerStat().apply {
 *             strength = if(player.isSprinting) 10.0 else 0.0
 *         }
 *     }
 * }
 */
interface StatProvider {

    /**
     * 为指定玩家生成状态数据
     *
     * @param player 目标玩家
     * @return PlayerStat对象包含此提供者生成的状态数据
     */
    fun provideStats(player: Player): PlayerStat
}