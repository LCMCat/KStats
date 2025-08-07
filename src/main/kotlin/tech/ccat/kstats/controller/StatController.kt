package tech.ccat.kstats.controller

import org.bukkit.entity.Player
import tech.ccat.kstats.api.PlayerStatAccessor
import tech.ccat.kstats.model.StatType
import tech.ccat.kstats.service.StatManager

class StatController(private val statManager: StatManager) : PlayerStatAccessor {
    override fun getAllStats(player: Player) = statManager.getAllStats(player)
    override fun getStat(player: Player, statType: StatType) = statManager.getStat(player, statType)
}