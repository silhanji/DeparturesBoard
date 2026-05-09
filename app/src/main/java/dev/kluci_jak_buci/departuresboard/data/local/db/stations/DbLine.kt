package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lines")
data class DbLine(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "direction")
    val direction: String,
    @ColumnInfo(name = "direction2")
    val direction2: String?,
)
