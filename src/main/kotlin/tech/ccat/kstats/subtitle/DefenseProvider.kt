package tech.ccat.kstats.subtitle

import org.bukkit.entity.Player
import tech.ccat.hsubtitle.api.TextProvider
import tech.ccat.hsubtitle.model.Info
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.StatType
import java.math.RoundingMode
import java.text.DecimalFormat

object DefenseProvider : TextProvider {
    override fun provideText(player: Player): Info {
        val format = DecimalFormat("#.#")
        format.roundingMode = RoundingMode.FLOOR

        val defense = format.format(KStats.instance.getStat(player, StatType.DEFENSE))

        if(defense == "0" || defense == null){
            return Info("", 0)
        }

        return Info("§a❈防御 ${defense}§r", 500)
    }
}