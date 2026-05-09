package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import androidx.room.Transaction

@Dao
interface StationDao {

    // We have to skip query validation as Room does not know about our custom levenshtein function
    @SkipQueryVerification
    @Query(
        """
            SELECT name, levenshtein(haystack, :needle) AS distance FROM stations
            ORDER BY distance ASC
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