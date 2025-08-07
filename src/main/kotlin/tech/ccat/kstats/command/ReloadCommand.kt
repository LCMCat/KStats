package tech.ccat.kstats.command

import org.bukkit.command.CommandSender
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.MessageFormatter

class ReloadCommand : AbstractCommand(
    name = "reload",
    permission = "kstats.admin",
    usage = "/kstats reload"
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        KStats.instance.configManager.reloadAll()
        sender.sendMessage(MessageFormatter.format("config-reloaded"))
        return true
    }
}