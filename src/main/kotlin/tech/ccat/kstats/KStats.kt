package tech.ccat.kstats

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import tech.ccat.hsubtitle.api.HSubTitleAPI
import tech.ccat.kstats.api.EntityStatProvider
import tech.ccat.kstats.api.KStatsAPI
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.command.CommandManager
import tech.ccat.kstats.command.ReloadCommand
import tech.ccat.kstats.command.ShowCommand
import tech.ccat.kstats.config.ConfigManager
import tech.ccat.kstats.event.ManaConsumptionEvent
import tech.ccat.kstats.listener.*
import tech.ccat.kstats.model.BaseEntityStat
import tech.ccat.kstats.model.StatType
import tech.ccat.kstats.service.CacheService
import tech.ccat.kstats.service.EntityStatManager
import tech.ccat.kstats.service.StatManager
import tech.ccat.kstats.subtitle.DefenseProvider
import tech.ccat.kstats.subtitle.HealthProvider
import tech.ccat.kstats.subtitle.ManaProvider
import java.util.concurrent.CopyOnWriteArrayList

class KStats : JavaPlugin(), KStatsAPI {

    internal lateinit var configManager: ConfigManager
    internal lateinit var cacheService: CacheService
    internal lateinit var statManager: StatManager
    internal lateinit var entityStatManager: EntityStatManager
    internal lateinit var commandManager: CommandManager

    private lateinit var subTitleApi: HSubTitleAPI

    private lateinit var healingListener: HealingListener

    companion object {
        internal lateinit var instance: KStats
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        configManager = ConfigManager().apply { setup() }
        cacheService = CacheService()
        statManager = StatManager(cacheService, configManager).apply {
            setDebounceDelay(configManager.statConfig.getDebounceDelay())
        }
        entityStatManager = EntityStatManager()

        registerListeners()

        commandManager = CommandManager().apply {
            registerCommand(ShowCommand())
            registerCommand(ReloadCommand())
        }
        this.getCommand("kstats")?.setExecutor(commandManager)

        Bukkit.getWorlds().forEach { world ->
            world.setGameRule(GameRule.NATURAL_REGENERATION, false)
        }

        registerSubTitles()

        server.servicesManager.register(
            KStatsAPI::class.java,
            this,
            this,
            ServicePriority.Normal
        )

        logger.info("KStats已启用。API版本: ${pluginMeta.version}")

        if(!Bukkit.getOnlinePlayers().isEmpty()){
            midInitPlayerStat()
        }
    }

    override fun onDisable() {
        statManager.unregisterAllProviders()
        entityStatManager.clearProvider()

        cacheService.clearCache()

        server.servicesManager.unregister(this)

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
            logger.info("已注册到 HSubTitle v${registration.plugin.pluginMeta.version}")
        } else {
            logger.warning("HSubTitle API 未找到，动作条功能将不可用")
        }
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()

        healingListener = HealingListener()

        pm.registerEvents(EntityDamageListener(), this)
        pm.registerEvents(CriticalHitListener(), this)
        pm.registerEvents(SpeedListener(), this)
        pm.registerEvents(healingListener, this)
        pm.registerEvents(StatUpdateListener(), this)
        pm.registerEvents(PlayerLoginListener(), this)
    }

    private fun midInitPlayerStat(){
        Bukkit.getOnlinePlayers().forEach {
            statManager.initPlayerStats(it)
            statManager.updateStats(it)

            healingListener.addPlayerToTask(it)
        }
    }

    override fun getAllStats(entity: LivingEntity): BaseEntityStat {
        return if (entity is Player) {
            statManager.getAllStats(entity)
        } else {
            entityStatManager.getAllStats(entity)
        }
    }

    override fun getStat(entity: LivingEntity, statType: StatType): Double {
        if (statType.isBaseStat()) {
            return if (entity is Player) {
                statManager.getAllStats(entity).getStatValue(statType)
            } else {
                entityStatManager.getStat(entity, statType)
            }
        }
        if (statType.isPlayerOnly()) {
            return if (entity is Player) {
                statManager.getAllStats(entity).getStatValue(statType)
            } else {
                0.0
            }
        }
        return 0.0
    }

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

    override fun setEntityProvider(provider: EntityStatProvider) {
        entityStatManager.setProvider(provider)
    }

    override fun clearEntityProvider() {
        entityStatManager.clearProvider()
    }

    override fun getEntityProvider(): EntityStatProvider? {
        return entityStatManager.getProvider()
    }
}
