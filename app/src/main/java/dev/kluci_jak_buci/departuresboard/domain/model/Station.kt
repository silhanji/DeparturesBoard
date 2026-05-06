package dev.kluci_jak_buci.departuresboard.domain.model

import kotlin.text.lines

@JvmInline
value class StationName(val value: String)

data class Station(
    val name: StationName,
    val platforms: List<Platform>,
) {
    /**
     * Gets all lines irrespective of platform.
     */
    fun getLines(): List<Line> {
        return platforms.flatMap{ it.lines }
    }
}