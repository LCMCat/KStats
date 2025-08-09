package tech.ccat.kstats.api

import org.bukkit.entity.Player
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.kstats.model.StatType
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList

/**
 * KStats插件的主要API接口，提供访问玩家状态数据和注册状态提供者的能力
 *
 * 其他插件可以通过[org.bukkit.plugin.ServicesManager]获取此接口的实例
 *
 * 使用示例：
 * val api = Bukkit.getServicesManager().getRegistration(KStatsAPI::class.java)?.provider
 * api?.registerProvider(myStatProvider)
 * ```
 */
interface KStatsAPI {

    /**
     * 获取玩家的完整状态信息
     *
     * @param player 目标玩家
     * @return 包含所有状态值的PlayerStat对象
     */
    fun getAllStats(player: Player): PlayerStat

    /**
     * 获取玩家的特定状态值
     *
     * @param player 目标玩家
     * @param statType 要获取的状态类型
     * @return 对应状态类型的数值
     */
    fun getStat(player: Player, statType: StatType): Double

    /**
     * 注册状态提供者
     *
     * 所有注册的提供者会被综合计算玩家属性
     *
     * @param provider 要注册的状态提供者实例
     */
    fun registerProvider(provider: StatProvider)

    /**
     * 取消注册状态提供者
     *
     * @param provider 要取消注册的状态提供者实例
     */
    fun unregisterProvider(provider: StatProvider)

    /**
     * 强制更新玩家的状态数据
     *
     * @param player 要更新的玩家
     */
    fun forceUpdate(player: Player)

    /**
     * 强制更新所有在线玩家的状态数据
     */
    fun forceUpdateAll()

    /**
     * 获取所有已注册的Provider
     */
    fun getRegisteredProviders(): CopyOnWriteArrayList<StatProvider>?
}