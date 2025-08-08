package tech.ccat.kstats.subtitle

import org.bukkit.entity.Player
import tech.ccat.hsubtitle.api.TextProvider
import tech.ccat.hsubtitle.model.Info
import tech.ccat.kstats.KStats
import tech.ccat.kstats.model.StatType
import java.math.RoundingMode
import java.text.DecimalFormat

object HealthProvider : TextProvider {
    override fun provideText(player: Player): Info {
        val format = DecimalFormat("#.#")
        format.roundingMode = RoundingMode.FLOOR

        val health = format.format(player.health)
        val maxHealth = format.format(KStats.instance.getStat(player, StatType.HEALTH))

        return Info("§c❤生命 ${health}/${maxHealth}§r", 1000)
    }
}