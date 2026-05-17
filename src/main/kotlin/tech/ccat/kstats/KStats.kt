package tech.ccat.kstats

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import tech.ccat.hsubtitle.api.HSubTitleAPI
import tech.ccat.kstats.api.KStatsAPI
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.command.CommandManager
import tech.ccat.kstats.command.ReloadCommand
import tech.ccat.kstats.command.ShowCommand
import tech.ccat.kstats.config.ConfigManager
import tech.ccat.kstats.event.ManaConsumptionEvent
import tech.ccat.kstats.listener.*
import tech.ccat.kstats.model.StatType
import tech.ccat.kstats.service.CacheService
import tech.ccat.kstats.service.StatManager
import tech.ccat.kstats.subtitle.DefenseProvider
import tech.ccat.kstats.subtitle.HealthProvider
import tech.ccat.kstats.subtitle.ManaProvider
import java.util.concurrent.CopyOnWriteArrayList

class KStats : JavaPlugin(), KStatsAPI {

    // 依赖注入
    internal lateinit var configManager: ConfigManager
    internal lateinit var cacheService: CacheService
    internal lateinit var statManager: StatManager
    internal lateinit var commandManager: CommandManager

    //外置API
    private lateinit var subTitleApi: HSubTitleAPI

    //一个监听器
    private lateinit var healingListener: HealingListener

    companion object {
        internal lateinit var instance: KStats
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        // 初始化核心模块
        configManager = ConfigManager().apply { setup() }
        cacheService = CacheService()
        statManager = StatManager(cacheService, configManager).apply {
            setDebounceDelay(configManager.statConfig.getDebounceDelay())
        }

        // 注册事件监听器
        registerListeners()

        // 初始化命令系统
        commandManager = CommandManager().apply {
            registerCommand(ShowCommand())
            registerCommand(ReloadCommand())
        }
        this.getCommand("kstats")?.setExecutor(commandManager)

        // 关闭原版恢复机制
        Bukkit.getWorlds().forEach { world ->
            world.setGameRule(GameRule.NATURAL_REGENERATION, false)
        }

        //注册Provider
        registerSubTitles()

        // 将自身注册为服务提供者 (Bukkit方式)
        server.servicesManager.register(
            KStatsAPI::class.java,
            this,
            this,
            ServicePriority.Normal
        )

        logger.info("KStats已启用。API版本: ${getDescription().version}")

        if(!Bukkit.getOnlinePlayers().isEmpty()){
            midInitPlayerStat()
        }
    }

    /**
     * 插件禁用时调用
     */
    override fun onDisable() {
        // 停止所有任务
        statManager.unregisterAllProviders()

        // 清理缓存
        cacheService.clearCache()

        // 取消服务注册
        server.servicesManager.unregister(this)

        //卸载Provider
        if (::subTitleApi.isInitialized) {
            subTitleApi.unregisterProvider(HealthProvider)
            subTitleApi.unregisterProvider(DefenseProvider)
            subTitleApi.unregisterProvider(ManaProvider)
        }

        logger.info("KStats已禁用")
    }

    private fun registerSubTitles(){
        val registration = Bukkit.getServicesManager().getRegistration(HSubTitleAPI::class.java)
        if (registration != null) {
            subTitleApi = registration.provider
            subTitleApi.registerProvider(HealthProvider)
            subTitleApi.registerProvider(DefenseProvider)
            subTitleApi.registerProvider(ManaProvider)
            logger.info("已注册到 HSubTitle v${registration.plugin.getDescription().version}")
        } else {
            logger.warning("HSubTitle API 未找到，动作条功能将不可用")
        }
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()

        healingListener = HealingListener()

        pm.registerEvents(DamageListener(), this)
        pm.registerEvents(CriticalHitListener(), this)
        pm.registerEvents(SpeedListener(), this)
        pm.registerEvents(healingListener, this)
        pm.registerEvents(StatUpdateListener(), this)
        pm.registerEvents(PlayerLoginListener(), this)
    }

    //如果插件在服务器运行中被Bukkit的插件管理器重载，则使用次方法加载未被监听器加载的玩家数据
    private fun midInitPlayerStat(){
        Bukkit.getOnlinePlayers().forEach {
            statManager.initPlayerStats(it)
            statManager.updateStats(it)

            healingListener.addPlayerToTask(it)
        }
    }

    // ============ KStatsAPI 实现 ============

    override fun getAllStats(player: Player) = statManager.getAllStats(player)

    override fun getStat(player: Player, statType: StatType) = statManager.getStat(player, statType)

    override fun registerProvider(provider: StatProvider) {
        statManager.registerProvider(provider)
        forceUpdateAll()
    }

    override fun unregisterProvider(provider: StatProvider) {
        statManager.unregisterProvider(provider)
        forceUpdateAll()
    }

    override fun forceUpdate(player: Player) {
        statManager.updateStats(player)
    }

    override fun forceUpdateAll() {
        Bukkit.getOnlinePlayers().forEach(::forceUpdate)
    }

    override fun getRegisteredProviders(): CopyOnWriteArrayList<StatProvider>? {
        return statManager.getRegisteredProviders()
    }

    override fun requestUpdate(player: Player) {
        statManager.requestUpdate(player)
    }

    override fun requestUpdateAll() {
        Bukkit.getOnlinePlayers().forEach { requestUpdate(it) }
    }

    override fun getMana(player: Player): Double {
        val maxMana = getMaxMana(player)
        return cacheService.getMana(player.uniqueId).coerceAtMost(maxMana)
    }

    override fun getMaxMana(player: Player): Double {
        return getStat(player, StatType.WISDOM)
    }

    override fun consumeMana(player: Player, amount: Double): Boolean {
        return consumeMana(player, amount, false)
    }

    override fun consumeMana(player: Player, amount: Double, showAlert: Boolean): Boolean {
        return consumeMana(player, amount, "", showAlert)
    }

    override fun consumeMana(player: Player, amount: Double, reason: String): Boolean {
        return consumeMana(player, amount, reason, true)
    }

    override fun consumeMana(player: Player, amount: Double, reason: String, showAlert: Boolean): Boolean {
        val currentMana = getMana(player)
        if (currentMana < amount) {
            if (showAlert) {
                ManaProvider.showAlert(player.uniqueId, "§c§l法力不足", 2000L)
            }
            return false
        }
        cacheService.setMana(player.uniqueId, currentMana - amount)

        if (reason.isNotEmpty()) {
            ManaProvider.showConsumption(player.uniqueId, amount, reason, 2000L)
        }

        Bukkit.getPluginManager().callEvent(ManaConsumptionEvent(player, amount, reason))
        return true
    }

    override fun restoreMana(player: Player, amount: Double) {
        val maxMana = getMaxMana(player)
        val newMana = (getMana(player) + amount).coerceAtMost(maxMana)
        cacheService.setMana(player.uniqueId, newMana)
    }

    override fun setMana(player: Player, amount: Double) {
        val maxMana = getMaxMana(player)
        val clampedAmount = amount.coerceIn(0.0, maxMana)
        cacheService.setMana(player.uniqueId, clampedAmount)
    }
}