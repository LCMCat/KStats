package tech.ccat.kstats.util

import org.bukkit.entity.Player
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat
import java.util.concurrent.CopyOnWriteArrayList

object SummationEngine {
    fun calculateStats(providers: CopyOnWriteArrayList<StatProvider>, player: Player, baseStats: PlayerStat): PlayerStat {
        val result = baseStats.copy()

        providers.forEach { provider ->
            val provided = provider.provideStats(player)
            result.addAllStats(provided)
        }

        return result
    }
}
