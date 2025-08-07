package tech.ccat.kstats.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.util.CombatUtil

class DamageListener : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val stats = KStats.instance.statManager.getAllStats(damager)

        val baseDamage = stats.baseDamage
        val strength = stats.strength
        val critChance = stats.critChance
        val critDamage = stats.critDamage
        val damageMultiplier = stats.damageMultiplier

        val finalDamage = CombatUtil.calculateDamage(
            baseDamage,
            strength,
            critChance,
            critDamage,
            damageMultiplier
        )

        event.damage = finalDamage
    }
}