package dev.kluci_jak_buci.departuresboard.domain.model

@JvmInline
value class StationName(val value: String)

data class Station(
    val name: StationName,
    val platforms: List<Platform>,
)