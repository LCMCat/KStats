package tech.ccat.kstats.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ConfigManager(private val plugin: JavaPlugin) {
    private val configs = mutableMapOf<String, YamlConfiguration>()
    private val configFiles = mutableMapOf<String, File>()

    lateinit var statConfig: StatConfig
    lateinit var messageConfig: MessageConfig

    fun setup() {
        saveDefaultConfig("config.yml")
        saveDefaultConfig("messages.yml")

        reloadAll()
    }

    private fun saveDefaultConfig(fileName: String) {
        val file = File(plugin.dataFolder, fileName)
        if (!file.exists()) {
            plugin.saveResource(fileName, false)
        }
        configFiles[fileName] = file
        configs[fileName] = YamlConfiguration.loadConfiguration(file)
    }

    fun reloadAll() {
        configFiles.forEach { (name, file) ->
            configs[name] = YamlConfiguration.loadConfiguration(file)
        }

        statConfig = StatConfig(configs["config.yml"]!!)
        messageConfig = MessageConfig(configs["messages.yml"]!!)
    }
}