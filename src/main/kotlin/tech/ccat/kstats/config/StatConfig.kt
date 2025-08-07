package tech.ccat.kstats.config

import org.bukkit.configuration.ConfigurationSection
import tech.ccat.kstats.model.PlayerStat

class StatConfig(private val config: ConfigurationSection) {
    fun getDefaultStats() = PlayerStat(
        health = config.getDouble("defaults.health", 20.0),
        defense = config.getDouble("defaults.defense", 0.0),
        strength = config.getDouble("defaults.strength", 0.0),
        speed = config.getDouble("defaults.speed", 100.0),
        critChance = config.getDouble("defaults.crit-chance", 30.0),
        critDamage = config.getDouble("defaults.crit-damage", 50.0),
        wisdom = config.getDouble("defaults.wisdom", 100.0),
        baseDamage = config.getDouble("defaults.base-damage", 1.0),
        damageMultiplier = config.getDouble("defaults.damage-multiplier", 1.0),
        healing = config.getDouble("defaults.healing", 100.0)
    )
}