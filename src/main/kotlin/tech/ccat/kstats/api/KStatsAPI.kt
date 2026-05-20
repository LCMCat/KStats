package tech.ccat.kstats.api

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import tech.ccat.kstats.model.BaseEntityStat
import tech.ccat.kstats.model.StatType
import java.util.concurrent.CopyOnWriteArrayList

/**
 * KStats核心API接口，提供属性查询、属性提供者注册、法力系统和生命值系统的统一入口
 *
 * 通过Bukkit ServicesManager获取实例：
 * ```kotlin
 * val api = Bukkit.getServicesManager().getRegistration(KStatsAPI::class.java)?.provider
 * ```
 */
interface KStatsAPI {

    /**
     * 获取实体的完整属性数据
     *
     * @param entity 目标实体（玩家或其他生物实体）
     * @return BaseEntityStat对象包含所有基础属性，若为玩家则包含玩家专属属性
     */
    fun getAllStats(entity: LivingEntity): BaseEntityStat

    /**
     * 获取实体的指定属性值
     *
     * @param entity 目标实体
     * @param statType 属性类型
     * @return 对应的属性数值，若实体为非玩家且请求玩家专属属性则返回0.0
     */
    fun getStat(entity: LivingEntity, statType: StatType): Double

    /**
     * 注册属性提供者，注册后将参与玩家属性的计算
     *
     * @param provider 属性提供者实例
     */
    fun registerProvider(provider: StatProvider)

    /**
     * 取消注册属性提供者
     *
     * @param provider 要取消注册的属性提供者实例
     */
    fun unregisterProvider(provider: StatProvider)

    /**
     * 强制更新指定玩家的属性数据（立即触发重新计算）
     *
     * @param player 目标玩家
     */
    fun forceUpdate(player: Player)

    /**
     * 强制更新所有在线玩家的属性数据
     */
    fun forceUpdateAll()

    /**
     * 获取所有已注册的属性提供者
     *
     * @return 已注册的属性提供者列表，若无则返回null
     */
    fun getRegisteredProviders(): CopyOnWriteArrayList<StatProvider>?

    /**
     * 请求更新指定玩家的属性数据（带防抖，适用于高频触发场景）
     *
     * @param player 目标玩家
     */
    fun requestUpdate(player: Player)

    /**
     * 请求更新所有在线玩家的属性数据（带防抖）
     */
    fun requestUpdateAll()

    /**
     * 获取玩家当前法力值
     *
     * @param player 目标玩家
     * @return 当前法力值，不超过最大法力值
     */
    fun getMana(player: Player): Double

    /**
     * 获取玩家最大法力值（等于WISDOM属性值）
     *
     * @param player 目标玩家
     * @return 最大法力值
     */
    fun getMaxMana(player: Player): Double

    /**
     * 消耗玩家法力值
     *
     * @param player 目标玩家
     * @param amount 消耗量
     * @return 是否消耗成功，法力不足时返回false
     */
    fun consumeMana(player: Player, amount: Double): Boolean

    /**
     * 消耗玩家法力值
     *
     * @param player 目标玩家
     * @param amount 消耗量
     * @param showAlert 法力不足时是否显示警告
     * @return 是否消耗成功
     */
    fun consumeMana(player: Player, amount: Double, showAlert: Boolean): Boolean

    /**
     * 消耗玩家法力值
     *
     * @param player 目标玩家
     * @param amount 消耗量
     * @param reason 消耗原因（用于显示消耗提示）
     * @return 是否消耗成功
     */
    fun consumeMana(player: Player, amount: Double, reason: String): Boolean

    /**
     * 消耗玩家法力值
     *
     * @param player 目标玩家
     * @param amount 消耗量
     * @param reason 消耗原因（用于显示消耗提示）
     * @param showAlert 法力不足时是否显示警告
     * @return 是否消耗成功
     */
    fun consumeMana(player: Player, amount: Double, reason: String, showAlert: Boolean): Boolean

    /**
     * 恢复玩家法力值
     *
     * @param player 目标玩家
     * @param amount 恢复量，不会超过最大法力值
     */
    fun restoreMana(player: Player, amount: Double)

    /**
     * 设置玩家法力值
     *
     * @param player 目标玩家
     * @param amount 要设置的法力值，会被限制在0到最大法力值之间
     */
    fun setMana(player: Player, amount: Double)

    /**
     * 设置实体属性提供者，用于为非玩家实体提供属性数据
     *
     * @param provider 实体属性提供者实例
     */
    fun setEntityProvider(provider: EntityStatProvider)

    /**
     * 清除已注册的实体属性提供者
     */
    fun clearEntityProvider()

    /**
     * 获取当前注册的实体属性提供者
     *
     * @return 实体属性提供者实例，若未注册则返回null
     */
    fun getEntityProvider(): EntityStatProvider?

    /**
     * 获取玩家真实生命值
     *
     * @param player 目标玩家
     * @return 真实生命值
     */
    fun getRealHealth(player: Player): Double

    /**
     * 设置玩家真实生命值
     *
     * @param player 目标玩家
     * @param health 要设置的生命值，会被限制在0到最大生命值之间
     */
    fun setRealHealth(player: Player, health: Double)

    /**
     * 获取玩家最大生命值
     *
     * @param player 目标玩家
     * @return 最大生命值（HEALTH属性值）
     */
    fun getMaxHealth(player: Player): Double

    /**
     * 对玩家造成伤害
     *
     * @param player 目标玩家
     * @param amount 伤害量
     */
    fun damagePlayer(player: Player, amount: Double)

    /**
     * 治疗玩家
     *
     * @param player 目标玩家
     * @param amount 治疗量
     */
    fun healPlayer(player: Player, amount: Double)
}
