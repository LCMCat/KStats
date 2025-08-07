// tech/ccat/kstats/KStats.kt
package tech.ccat.kstats

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import tech.ccat.kstats.listener.SpeedListener
import tech.ccat.kstats.command.CommandManager
import tech.ccat.kstats.command.ReloadCommand
import tech.ccat.kstats.command.ShowCommand
import tech.ccat.kstats.config.ConfigManager
import tech.ccat.kstats.controller.StatController
import tech.ccat.kstats.listener.*
import tech.ccat.kstats.service.CacheService
import tech.ccat.kstats.service.StatManager

class KStats : JavaPlugin() {
    // 依赖注入
    lateinit var configManager: ConfigManager
    lateinit var cacheService: CacheService
    lateinit var statManager: StatManager
    lateinit var statController: StatController
    lateinit var commandManager: CommandManager

    companion object {
        lateinit var instance: KStats
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        // 初始化核心模块
        configManager = ConfigManager(this).apply { setup() }
        cacheService = CacheService()
        statManager = StatManager(cacheService, configManager)
        statController = StatController(statManager)

        // 注册事件监听器
        registerListeners()

        // 初始化命令系统
        commandManager = CommandManager(this).apply {
            registerCommand(ShowCommand())
            registerCommand(ReloadCommand())
        }
        this.getCommand("kstats")?.setExecutor(commandManager)


        // 关闭原版恢复
        Bukkit.getWorlds().forEach { world ->
            world.setGameRuleValue("naturalRegeneration", "false")
        }
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(DamageListener(), this)
        pm.registerEvents(CriticalHitListener(), this)
        pm.registerEvents(SpeedListener(), this)
        pm.registerEvents(HealingListener(), this)
        pm.registerEvents(StatUpdateListener(), this)
        pm.registerEvents(PlayerLoginListener(), this)
    }

    override fun onDisable() {
        cacheService.clearCache()
    }
}