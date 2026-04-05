package dev.kluci_jak_buci.departuresboard.domain.model

/**
 * Name of the line, sometimes referenced as "short name"
 */
@JvmInline
value class LineName(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Line name can not be blank"
        }
    }
}

/**
 * @param name Name of the line - human-readable
 * @param type Type of the line
 * @param directions List of names of all terminal stations for the line. Usually contains single
 * value, but some lines can have multiple terminal stations in one direction.
 */
data class Line(
    val name: LineName,
    val type: LineType,
    val directions: List<String>,
)

enum class LineType {
    TRAM,
    BUS,
    METRO,
}