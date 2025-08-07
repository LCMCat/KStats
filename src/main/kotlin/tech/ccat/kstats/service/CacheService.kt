package tech.ccat.kstats.service

import tech.ccat.kstats.dao.PlayerStatDao
import tech.ccat.kstats.model.PlayerStat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CacheService  : PlayerStatDao {
    private val playerCache = ConcurrentHashMap<UUID, PlayerStat>()

    override fun getPlayerStats(uuid: UUID) = playerCache[uuid]

    override fun savePlayerStats(uuid: UUID, stats: PlayerStat) {
        playerCache[uuid] = stats
    }

    override fun removePlayer(uuid: UUID) {
        playerCache.remove(uuid)
    }

    override fun clearCache() {
        playerCache.clear()
    }
}