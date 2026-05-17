package tech.ccat.kstats.listener

import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.CombatUtil
import tech.ccat.kstats.util.MessageFormatter

class CriticalHitListener : Listener {
    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        val damager = when (event.damager) {
            is Player -> event.damager as Player
            is Projectile -> (event.damager as Projectile).shooter as? Player ?: return
            else -> return
        }
        val stats = KStats.instance.statManager.getAllStats(damager)

        if (CombatUtil.isCritical(stats.critChance)) {
            event.damage = event.damage * (1 + stats.critDamage / 100)
            damager.sendMessage(
                MessageFormatter.format("critical-hit", event.damage)
            )
        }
    }
}