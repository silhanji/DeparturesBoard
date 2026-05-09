package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "platforms")
data class DbPlatform(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "station_id")
    val stationId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
)

data class DbPlatformWithLines(
    @Embedded
    val platform: DbPlatform,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            DbLinePlatformCrossRef::class,
            parentColumn = "platform_id",
            entityColumn = "line_id"
        ),
    )
    val lines: List<DbLine>,
)
