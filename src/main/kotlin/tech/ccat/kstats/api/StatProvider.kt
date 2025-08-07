package tech.ccat.kstats.api

import org.bukkit.entity.Player
import tech.ccat.kstats.model.PlayerStat

interface StatProvider {
    fun provideStats(player: Player): PlayerStat
}