package dev.kluci_jak_buci.departuresboard.data.local.db.profiles

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.kluci_jak_buci.departuresboard.data.local.db.TimeConverter

@Database(
    entities = [DbProfile::class, DbSelectedLine::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TimeConverter::class)
abstract class ProfilesDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}