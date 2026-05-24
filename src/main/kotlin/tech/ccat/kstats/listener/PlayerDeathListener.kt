package tech.ccat.kstats.listener

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import tech.ccat.kstats.KStats
import tech.ccat.kstats.event.PlayerSetDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerSetDeathEvent) {
        val player = event.player
        val api = KStats.instance

        val respawnLocation = player.respawnLocation ?: player.world.spawnLocation
        player.teleport(respawnLocation)

        api.healPlayer(player)

        val deathMessage = buildDeathMessage(event)
        Bukkit.broadcastMessage(deathMessage)
    }

    private fun buildDeathMessage(event: PlayerSetDeathEvent): String {
        val playerName = event.player.name
        val cause = event.cause
        val killer = event.killer

        val killerName = when (killer) {
            is Player -> killer.name
            is Entity -> killer.name ?: "未知"
            else -> null
        }

        return when (cause) {
            EntityDamageEvent.DamageCause.VOID -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7被 §r§c$killerName§r§7击入虚空。"
                } else {
                    "§4☠ §r§c$playerName §r§7掉入了虚空。"
                }
            }
            EntityDamageEvent.DamageCause.FALL -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7在 §r§c$killerName§r§7的帮助下摔死了。"
                } else {
                    "§4☠ §r§c$playerName §r§7摔死了。"
                }
            }
            EntityDamageEvent.DamageCause.FLY_INTO_WALL -> {
                "§4☠ §r§c$playerName §r§7撞击墙壁身亡。"
            }
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA -> {
                "§4☠ §r§c$playerName §r§7被烧死了。"
            }
            EntityDamageEvent.DamageCause.DROWNING -> {
                "§4☠ §r§c$playerName §r§7溺水身亡。"
            }
            EntityDamageEvent.DamageCause.SUFFOCATION -> {
                "§4☠ §r§c$playerName §r§7窒息而死。"
            }
            EntityDamageEvent.DamageCause.CONTACT -> {
                "§4☠ §r§c$playerName §r§7被仙人掌扎死了。"
            }
            EntityDamageEvent.DamageCause.MAGIC -> {
                "§4☠ §r§c$playerName §r§7被魔法击杀。"
            }
            EntityDamageEvent.DamageCause.WITHER -> {
                "§4☠ §r§c$playerName §r§7凋零而死。"
            }
            EntityDamageEvent.DamageCause.POISON -> {
                "§4☠ §r§c$playerName §r§7中毒身亡。"
            }
            EntityDamageEvent.DamageCause.STARVATION -> {
                "§4☠ §r§c$playerName §r§7饿死了。"
            }
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7被 §r§c$killerName§r§7击杀。"
                } else {
                    "§4☠ §r§c$playerName §r§7被击杀。"
                }
            }
            EntityDamageEvent.DamageCause.PROJECTILE -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7被 §r§c$killerName§r§7射杀。"
                } else {
                    "§4☠ §r§c$playerName §r§7被射杀。"
                }
            }
            EntityDamageEvent.DamageCause.THORNS -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7被 §r§c$killerName§r§7的反伤击杀。"
                } else {
                    "§4☠ §r§c$playerName §r§7被反伤击杀。"
                }
            }
            EntityDamageEvent.DamageCause.HOT_FLOOR -> {
                "§4☠ §r§c$playerName §r§7被烫死了。"
            }
            EntityDamageEvent.DamageCause.CRAMMING -> {
                "§4☠ §r§c$playerName §r§7被挤压而死。"
            }
            EntityDamageEvent.DamageCause.DRYOUT -> {
                "§4☠ §r§c$playerName §r§7脱水而死。"
            }
            EntityDamageEvent.DamageCause.FREEZE -> {
                "§4☠ §r§c$playerName §r§7冻死了。"
            }
            EntityDamageEvent.DamageCause.KILL -> {
                if (killerName != null) {
                    "§4☠ §r§c$playerName §r§7被 §r§c$killerName§r§7击杀。"
                } else {
                    "§4☠ §r§c$playerName §r§7死亡了。"
                }
            }
            else -> {
                "§4☠ §r§c$playerName §r§7死亡了。"
            }
        }
    }
}
