package dev.kluci_jak_buci.departuresboard.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Transaction
    @Query(
        """
            SELECT * FROM profiles
            ORDER BY name ASC
        """
    )
    fun getAll(): Flow<List<DbProfileWithLines>>

    @Transaction
    @Query(
        """
            SELECT * FROM profiles
            WHERE id = :profileId
            ORDER BY name ASC
        """
    )
    fun get(profileId: String): Flow<DbProfileWithLines?>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: DbProfile, lines: List<DbSelectedLine>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lines: List<DbSelectedLine>)

    @Update
    suspend fun update(profile: DbProfile)

    @Query(
        """
            DELETE FROM profiles
            WHERE id = :profileId
        """
    )
    suspend fun delete(profileId: String)

    @Query(
        """
            DELETE FROM selected_lines
            WHERE profile_id = :profileId
        """
    )
    suspend fun deleteSelectedLines(profileId: String)
}