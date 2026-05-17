package tech.ccat.kstats.api

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import tech.ccat.kstats.model.BaseEntityStat
import tech.ccat.kstats.model.StatType
import java.util.concurrent.CopyOnWriteArrayList

interface KStatsAPI {

    fun getAllStats(entity: LivingEntity): BaseEntityStat

    fun getStat(entity: LivingEntity, statType: StatType): Double

    fun registerProvider(provider: StatProvider)

    fun unregisterProvider(provider: StatProvider)

    fun forceUpdate(player: Player)

    fun forceUpdateAll()

    fun getRegisteredProviders(): CopyOnWriteArrayList<StatProvider>?

    fun requestUpdate(player: Player)

    fun requestUpdateAll()

    fun getMana(player: Player): Double

    fun getMaxMana(player: Player): Double

    fun consumeMana(player: Player, amount: Double): Boolean

    fun consumeMana(player: Player, amount: Double, showAlert: Boolean): Boolean

    fun consumeMana(player: Player, amount: Double, reason: String): Boolean

    fun consumeMana(player: Player, amount: Double, reason: String, showAlert: Boolean): Boolean

    fun restoreMana(player: Player, amount: Double)

    fun setMana(player: Player, amount: Double)

    fun setEntityProvider(provider: EntityStatProvider)

    fun clearEntityProvider()

    fun getEntityProvider(): EntityStatProvider?
}
