package dev.kluci_jak_buci.departuresboard.data.local.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime

class TimeConverter {

    @TypeConverter
    fun fromString(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun toString(value: LocalTime?): String? {
        return value?.toString()
    }
}