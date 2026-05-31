package tech.ccat.kstats.config

import org.bukkit.configuration.ConfigurationSection

class MessageConfig(private val config: ConfigurationSection) {
    private val deathPrefix: String = config.getString("death.prefix", "&4☠")?.replace("&", "§") ?: "☠"
    private val playerColor: String = config.getString("death.player-color", "&c")?.replace("&", "§") ?: "§c"
    private val textColor: String = config.getString("death.text-color", "&7")?.replace("&", "§") ?: "§7"
    private val killerColor: String = config.getString("death.killer-color", "&c")?.replace("&", "§") ?: "§c"

    fun getMessage(key: String, vararg args: Any): String {
        val raw = config.getString(key, "")
            ?.replace("&", "§")
        if (raw == null) {
            return "null"
        }
        return if (args.isNotEmpty()) String.format(raw, *args) else raw
    }

    fun getDeathMessage(messageKey: String, playerName: String, killerName: String? = null): String {
        val template = getMessage(messageKey)
        val content = if (killerName != null) {
            String.format(template, "$playerColor$playerName§r", "$killerColor$killerName§r")
        } else {
            String.format(template, "$playerColor$playerName§r")
        }
        return "$deathPrefix $textColor$content§r"
    }
}
