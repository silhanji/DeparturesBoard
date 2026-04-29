package dev.kluci_jak_buci.departuresboard.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.LocalTime

@Entity(tableName = "profiles")
data class DbProfile(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "time_filter_from")
    val timeFilterFrom: LocalTime?,
    @ColumnInfo(name = "time_filter_to")
    val timeFilterTo: LocalTime?,
    @ColumnInfo(name = "vehicle_filter_wheel_chair_accessible")
    val vehicleFilterWheelChairAccessible: Boolean?,
    @ColumnInfo(name = "vehicle_filter_air_conditioned")
    val vehicleFilterAirConditioned: Boolean?,
)

data class DbProfileWithLines(
    @Embedded
    val profile: DbProfile,
    @Relation(
        parentColumn = "id",
        entityColumn = "profile_id",
    )
    val selectedLines: List<DbSelectedLine>,
)