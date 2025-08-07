package tech.ccat.kstats.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.MessageFormatter

class ShowCommand : AbstractCommand(
    name = "show",
    usage = "/kstats show [player]",
    minArgs = 0
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        val target = if (args.size > 1) {
            Bukkit.getPlayer(args[1]) ?: run {
                sender.sendMessage(MessageFormatter.format("player-not-found"))
                return true
            }
        } else {
            if(sender !is Player){
                sender.sendMessage(MessageFormatter.format("player-command"))
                return true
            } else {
                sender
            }
        }

        val stats = KStats.instance.statManager.getAllStats(target)

        val message =
            MessageFormatter.format("stat-header", target.name) + '\n' +
            MessageFormatter.format("health-display", stats.health) + '\n' +
            MessageFormatter.format("defense-display", stats.defense) + '\n' +
            MessageFormatter.format("strength-display", stats.strength) + '\n' +
            MessageFormatter.format("speed-display", stats.speed) + '\n' +
            MessageFormatter.format("crit-chance-display", stats.critChance) + '\n' +
            MessageFormatter.format("crit-damage-display", stats.critDamage) + '\n' +
            MessageFormatter.format("wisdom-display", stats.wisdom) + '\n' +
            MessageFormatter.format("base-damage-display", stats.baseDamage) + '\n' +
            MessageFormatter.format("damage-multiplier-display", stats.damageMultiplier) + '\n' +
            MessageFormatter.format("healing-display", stats.healing)

        sender.sendMessage(message)
        return true
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }
    }
}