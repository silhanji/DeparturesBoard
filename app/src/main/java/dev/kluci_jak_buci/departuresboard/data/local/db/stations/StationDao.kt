package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StationDao {

    @Query(
        """
            SELECT name FROM stations
            WHERE name LIKE '%' || :needle || '%'
            LIMIT :limit
        """
    )
    suspend fun getStationNames(needle: String, limit: Int): Array<DbStationName>

    @Transaction
    @Query(
        """
            SELECT * FROM stations
            WHERE name = :name
        """
    )
    suspend fun getStation(name: String): DbStationWithPlatforms?
}