package tech.ccat.kstats.subtitle

import org.bukkit.entity.Player
import tech.ccat.hsubtitle.api.TextProvider
import tech.ccat.hsubtitle.model.Info
import tech.ccat.kstats.KStats
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.floor

data class ManaAlert(
    val message: String,
    val expireTime: Long
)

data class ManaConsumption(
    val amount: Double,
    val reason: String,
    val expireTime: Long
)

object ManaProvider : TextProvider {
    private val alertCache = ConcurrentHashMap<UUID, ManaAlert>()
    private val consumptionCache = ConcurrentHashMap<UUID, ManaConsumption>()

    fun showAlert(uuid: UUID, message: String, durationMillis: Long) {
        val expireTime = System.currentTimeMillis() + durationMillis
        alertCache[uuid] = ManaAlert(message, expireTime)
    }

    fun clearAlert(uuid: UUID) {
        alertCache.remove(uuid)
    }

    fun showConsumption(uuid: UUID, amount: Double, reason: String, durationMillis: Long) {
        val expireTime = System.currentTimeMillis() + durationMillis
        consumptionCache[uuid] = ManaConsumption(amount, reason, expireTime)
    }

    fun clearConsumption(uuid: UUID) {
        consumptionCache.remove(uuid)
    }

    private fun getAlert(uuid: UUID): String? {
        val alert = alertCache[uuid] ?: return null
        if (System.currentTimeMillis() > alert.expireTime) {
            alertCache.remove(uuid)
            return null
        }
        return alert.message
    }

    private fun getConsumption(uuid: UUID): ManaConsumption? {
        val consumption = consumptionCache[uuid] ?: return null
        if (System.currentTimeMillis() > consumption.expireTime) {
            consumptionCache.remove(uuid)
            return null
        }
        return consumption
    }

    override fun provideText(player: Player): Info {
        val alert = getAlert(player.uniqueId)
        if (alert != null) {
            return Info(alert, 200)
        }

        val maxMana = KStats.instance.getMaxMana(player)
        if (maxMana < 1) {
            return Info("", 200)
        }

        val mana = floor(KStats.instance.getMana(player)).toInt()
        val maxManaInt = floor(maxMana).toInt()

        val consumption = getConsumption(player.uniqueId)
        if (consumption != null) {
            val amountInt = floor(consumption.amount).toInt()
            return Info("§b✦智慧 ${mana}/${maxManaInt} (-${amountInt} ${consumption.reason})§r", 200)
        }

        return Info("§b✦智慧 ${mana}/${maxManaInt}§r", 200)
    }
}
