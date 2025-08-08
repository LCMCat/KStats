KStats
---
*A Modern and Lightweight Player Stat Management Plugin for Minecraft Servers*

## 📊 Features
- **Core Player Stats**: Health, Defense, Strength, Speed, Crit Chance, Crit Damage, Base Damage, Damage Multiplier, Mending
- ⚠️ *Wisdom stat interaction is currently in development*

## 🧮 Stat Calculation Architecture
Player attributes are computed using a centralized **Total Aggregator** system:
1. **Extensible API**: Provides interfaces/abstract classes for plugins to implement custom **Sub-Aggregators**
2. **Attribute Compilation**:
    - Total Aggregator collects data from all registered Sub-Aggregators
    - Sums values from all contributors
    - Updates cached player attributes
3. **Event-Driven Updates**:
    - Triggers `AttributeUpdateEvent` containing:
        - Player instance
        - Full attribute bundle
    - Allows attribute listeners to dynamically update player state (movement speed, health, etc.)
4. **Dynamic Synchronization**: Enables custom listeners to react to real-time stat changes

The example of data contributor can be found [here](http://github.com/LCMCat/KSDebugger).