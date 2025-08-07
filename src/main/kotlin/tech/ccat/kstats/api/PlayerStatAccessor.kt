package tech.ccat.kstats.api

import org.bukkit.entity.Player
import tech.ccat.kstats.model.PlayerStat
import tech.ccat.kstats.model.StatType

interface PlayerStatAccessor {
    fun getAllStats(player: Player): PlayerStat
    fun getStat(player: Player, statType: StatType): Double
}