package tech.ccat.kstats.service

import org.bukkit.entity.Player
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat
import java.util.concurrent.CopyOnWriteArrayList

class SummationEngine (
    val providers: CopyOnWriteArrayList<StatProvider>
){

    fun calculateStats(player: Player, baseStats: PlayerStat): PlayerStat {
        // 创建基础属性副本
        val result = baseStats.copy()

        // 遍历所有属性提供器进行加和
        providers.forEach { provider ->
            val provided = provider.provideStats(player)
            result.addAllStats(provided)
        }

        return result
    }
}