package dev.kluci_jak_buci.departuresboard.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "selected_lines",
    foreignKeys = [
        ForeignKey(
            entity = DbProfile::class,
            parentColumns = [ "id" ],
            childColumns = [ "profile_id" ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbSelectedLine(
    // Room requires the entity to have a primary
    // key, even though we will not use it
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "profile_id")
    val profileId: String,

    @ColumnInfo(name = "line")
    val line: String,
    @ColumnInfo(name = "platform")
    val platform: String,
)