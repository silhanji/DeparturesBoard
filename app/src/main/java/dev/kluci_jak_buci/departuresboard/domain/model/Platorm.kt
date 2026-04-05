package dev.kluci_jak_buci.departuresboard.domain.model

/**
 * Unique ID of given platform. Often referenced as StopId or GtfsId in
 * PID data.
 */
@JvmInline
value class PlatformId(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Platform ID can not be blank"
        }
    }
}

/**
 * @param id ID of the platform
 * @param name Name of the platform (often single letter value)
 * @param lines Lines which pass through this platform
 * @param position Position of the platform
 */
data class Platform(
    val id: PlatformId,
    val name: String,
    val lines: List<Line>,
    val position: GeoPosition,
)