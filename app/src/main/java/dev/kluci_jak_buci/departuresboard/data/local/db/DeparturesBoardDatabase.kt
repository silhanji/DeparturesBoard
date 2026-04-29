package dev.kluci_jak_buci.departuresboard.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DbProfile::class, DbSelectedLine::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TimeConverter::class)
abstract class DeparturesBoardDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}