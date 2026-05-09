package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.kluci_jak_buci.departuresboard.data.local.db.TimeConverter

@Database(
    entities = [
        DbStation::class, DbLine::class, DbPlatform::class,
        DbLinePlatformCrossRef::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(TimeConverter::class)
abstract class StationsDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}