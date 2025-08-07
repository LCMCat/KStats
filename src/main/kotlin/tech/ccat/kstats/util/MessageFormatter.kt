package tech.ccat.kstats.util

import tech.ccat.kstats.KStats

object MessageFormatter {
    fun format(key: String, vararg args: Any): String {
        val raw = KStats.instance.configManager.messageConfig.getMessage(key)
        return raw.format(*args)
    }
}