package tech.ccat.kstats.service

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.config.ConfigManager
import tech.ccat.kstats.event.StatUpdateEvent
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.kstats.model.StatType
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 状态管理器，负责：
 * 1. 管理所有状态提供者
 * 2. 计算并缓存玩家状态值
 * 3. 提供状态查询接口
 *
 * @property cacheService 缓存服务
 * @property configManager 配置管理器
 */
class StatManager(
    private val cacheService: CacheService,
    private val configManager: ConfigManager
) {

    private val logger = Bukkit.getLogger()

    private val providers = CopyOnWriteArrayList<StatProvider>()
    private val summationEngine: SummationEngine = SummationEngine(providers)

    /**
     * 初始化玩家状态数据
     * @param player 目标玩家
     */
    fun initPlayerStats(player: Player) {
        if (cacheService.getPlayerStats(player.uniqueId) == null) {
            val baseStats = configManager.statConfig.getDefaultStats()
            cacheService.savePlayerStats(player.uniqueId, baseStats)
        }
    }

    /**
     * 注册状态提供者
     * @param provider 要注册的状态提供者
     */
    fun registerProvider(provider: StatProvider) {
        providers.add(provider)
        logger.fine("注册状态提供者: ${provider::class.simpleName}")
    }

    /**
     * 取消注册状态提供者
     * @param provider 要取消注册的状态提供者
     */
    fun unregisterProvider(provider: StatProvider) {
        providers.remove(provider)
        logger.fine("取消注册状态提供者: ${provider::class.simpleName}")
    }

    /**
     * 取消所有已注册的状态提供者
     */
    fun unregisterAllProviders() {
        providers.clear()
        logger.fine("已清除所有状态提供者")
    }

    /**
     * 强制更新玩家的状态数据
     * @param player 要更新的玩家
     */
    fun updateStats(player: Player) {
        // 获取基础配置
        val baseStats = configManager.statConfig.getDefaultStats()

        // 计算总属性
        val totalStats = summationEngine.calculateStats(player, baseStats)

        // 更新缓存并触发事件
        cacheService.savePlayerStats(player.uniqueId, totalStats)
        Bukkit.getPluginManager().callEvent(StatUpdateEvent(player, totalStats))
    }

    /**
     * 获取玩家的完整状态数据
     * @param player 目标玩家
     * @return PlayerStat对象包含所有状态数据
     */
    fun getAllStats(player: Player): PlayerStat {
        return cacheService.getPlayerStats(player.uniqueId) ?: PlayerStat()
    }

    /**
     * 获取玩家的特定状态值
     * @param player 目标玩家
     * @param statType 状态类型
     * @return 对应的状态数值
     */
    fun getStat(player: Player, statType: StatType): Double {
        return getAllStats(player).getStatValue(statType)
    }

    /**
     * 获取已注册的Provider
     * @return CopyOnWriteArrayList<StatProvider>()
     */
    fun getRegisteredProviders(): CopyOnWriteArrayList<StatProvider>? {
        return providers
    }
}