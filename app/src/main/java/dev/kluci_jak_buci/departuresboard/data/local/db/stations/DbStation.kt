package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "stations")
data class DbStation(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
)

data class DbStationWithPlatforms(
    @Embedded
    val station: DbStation,
    @Relation(
        entity = DbPlatform::class,
        parentColumn = "id",
        entityColumn = "station_id"
    )
    val platforms: List<DbPlatformWithLines>,
)

data class DbStationName(
    @ColumnInfo(name = "name")
    val name: String,
)
