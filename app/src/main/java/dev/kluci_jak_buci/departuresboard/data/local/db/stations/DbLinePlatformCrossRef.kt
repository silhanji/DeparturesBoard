package dev.kluci_jak_buci.departuresboard.data.local.db.stations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "platform_lines",
    primaryKeys = ["line_id", "platform_id"],
    indices = [
        Index("platform_id", unique = false),
        Index("line_id", unique = false)
    ]
)
data class DbLinePlatformCrossRef(
    @ColumnInfo(name = "line_id")
    val lineId: Int,
    @ColumnInfo(name = "platform_id")
    val platformId: String,
)