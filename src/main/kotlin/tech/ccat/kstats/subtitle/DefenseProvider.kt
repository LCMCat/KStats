package tech.ccat.kstats.subtitle

import org.bukkit.entity.Player
import tech.ccat.hsubtitle.api.TextProvider
import tech.ccat.hsubtitle.model.Info
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.StatType

object DefenseProvider : TextProvider {
    override fun provideText(player: Player): Info {
        val defense = KStats.instance.getStat(player, StatType.DEFENSE)
        return Info("§a❈防御 ${defense}§r", 1)
    }
}