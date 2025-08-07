// tech/ccat/kstats/dao/PlayerStatDao.kt
package tech.ccat.kstats.dao

import tech.ccat.kstats.model.PlayerStat
import java.util.*

interface PlayerStatDao {
    fun getPlayerStats(uuid: UUID): PlayerStat?
    fun savePlayerStats(uuid: UUID, stats: PlayerStat)
    fun removePlayer(uuid: UUID)
    fun clearCache()
}