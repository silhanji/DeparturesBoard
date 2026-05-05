package dev.kluci_jak_buci.departuresboard.domain.model

import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Unique ID for user defined profiles.
 */
@OptIn(ExperimentalUuidApi::class)
@JvmInline
value class ProfileId(val value: String) {

    init {
        require(value.isNotBlank()) {
            "Profile ID can not be blank"
        }
    }

    companion object {
        fun generate() = ProfileId(
            Uuid.random().toString()
        )
    }

}

/**
 * Each profile represents single station for which the user wishes to see the departures.
 *
 * @param id ID of the profile
 * @param name User defined name of the profile
 * @param selectedLines User defined list of lines which they wish to watch
 * @param timeFilter Optional filter, allows user to specify at which time the profile is
 * relevant to them
 * @param vehicleFilter Optional filter, allows user to specify which vehicles are relevant to them
 */
data class Profile(
    val id: ProfileId,
    val name: String,
    val selectedLines: List<SelectedLine>,
    val timeFilter: TimeFilter?,
    val vehicleFilter: VehicleFilter?,
) {
    init {
        require(name.isNotBlank()) {
            "Profile name can not be blank"
        }
        require(selectedLines.isNotEmpty()) {
            "Profile must have at least one defined line"
        }
    }
}

/**
 * Identifies departures the user is interested in.
 *
 * @param line Line the user wishes to watch
 * @param platform ID from which the line leaves
 */
data class SelectedLine(
    val line: LineName,
    val platform: PlatformId,
)

data class TimeFilter(
    val from: LocalTime,
    val to: LocalTime,
)

data class VehicleFilter(
    val wheelChairAccessible: Boolean?,
    val airConditioned: Boolean?,
)