package tech.ccat.kstats.service

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.kstats.api.PlayerStatAccessor
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.config.ConfigManager
import tech.ccat.kstats.event.StatUpdateEvent
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.kstats.model.StatType
import java.util.concurrent.CopyOnWriteArrayList

class StatManager(
    private val cacheService: CacheService,
    private val configManager: ConfigManager
) : PlayerStatAccessor {
    private val providers = CopyOnWriteArrayList<StatProvider>()
    private val summationEngine: SummationEngine = SummationEngine(providers)

    fun initPlayerStats(player: Player) {
        if (cacheService.getPlayerStats(player.uniqueId) == null) {
            val baseStats = configManager.statConfig.getDefaultStats()
            cacheService.savePlayerStats(player.uniqueId, baseStats)
        }
    }

    fun getProviders(): CopyOnWriteArrayList<StatProvider>{
        return providers
    }

    fun registerProvider(provider: StatProvider) {
        providers.add(provider)
    }

    fun unregisterProvider(provider: StatProvider) {
        providers.remove(provider)
    }

    fun updateStats(player: Player) {
        // 获取基础配置
        val baseStats = configManager.statConfig.getDefaultStats()

        // 计算总属性
        val totalStats = summationEngine.calculateStats(player, baseStats)

        println("total health: " + totalStats.health)

        // 更新缓存并触发事件
        cacheService.savePlayerStats(player.uniqueId, totalStats)
        Bukkit.getPluginManager().callEvent(StatUpdateEvent(player, totalStats))
    }

    override fun getAllStats(player: Player): PlayerStat {
        return cacheService.getPlayerStats(player.uniqueId) ?: PlayerStat()
    }

    override fun getStat(player: Player, statType: StatType): Double {
        return getAllStats(player).getStatValue(statType)
    }

}