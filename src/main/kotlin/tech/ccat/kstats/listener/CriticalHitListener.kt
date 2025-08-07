package tech.ccat.kstats.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.CombatUtil
import tech.ccat.kstats.util.MessageFormatter

class CriticalHitListener : Listener {
    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val stats = KStats.instance.statManager.getAllStats(damager)

        if (CombatUtil.isCritical(stats.critChance)) {
            event.damage = event.damage * (1 + stats.critDamage / 100)
            damager.sendMessage(
                MessageFormatter.format("critical-hit", event.damage)
            )
        }
    }
}