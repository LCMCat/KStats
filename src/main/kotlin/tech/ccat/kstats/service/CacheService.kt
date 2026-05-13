package tech.ccat.kstats.service

import tech.ccat.kstats.dao.PlayerStatDao
import tech.ccat.kstats.model.PlayerStat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CacheService  : PlayerStatDao {
    private val playerCache = ConcurrentHashMap<UUID, PlayerStat>()
    private val manaCache = ConcurrentHashMap<UUID, Double>()

    override fun getPlayerStats(uuid: UUID) = playerCache[uuid]

    override fun savePlayerStats(uuid: UUID, stats: PlayerStat) {
        playerCache[uuid] = stats
    }

    override fun removePlayer(uuid: UUID) {
        playerCache.remove(uuid)
        manaCache.remove(uuid)
    }

    override fun clearCache() {
        playerCache.clear()
        manaCache.clear()
    }

    fun getMana(uuid: UUID): Double {
        return manaCache.getOrDefault(uuid, 100.0)
    }

    fun setMana(uuid: UUID, amount: Double) {
        manaCache[uuid] = amount
    }
}
