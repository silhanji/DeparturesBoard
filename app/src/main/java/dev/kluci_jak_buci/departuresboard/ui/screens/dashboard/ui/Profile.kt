package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.DashboardDeparture
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.DashboardProfile
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes


@Composable
fun Profile(
    profile: DashboardProfile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showNextDepartures: Boolean = true,
    nextDeparturesCount: Int = 3,
) {
    val departure = profile.departures.firstOrNull()

    Surface(
        shape = RoundedCornerShape(32.dp),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        onClick = onClick,
    ) {
        Column(modifier.padding(16.dp)) {
            FirstDeparture(
                profile.name,
                departure,
            )

            if(showNextDepartures) {
                for(i in 1 until nextDeparturesCount.coerceAtMost(profile.departures.size)) {
                    val spacerSize = if(i == 1) 24.dp else 16.dp

                    val nextDeparture = profile.departures[i]
                    Spacer(modifier = Modifier.height(spacerSize))
                    NextDeparture(nextDeparture)
                }
            }
        }
    }
}

@Composable
private fun FirstDeparture(
    profileName: String,
    departure: DashboardDeparture?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        LineBadge(
            line = departure?.line,
        )

        Column(
            modifier = Modifier.weight(1.0f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = profileName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            if(departure != null) {
                DepartureDetails(departure)
            } else {
                Text(
                    text = "No departures in next hour",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        if(departure != null) {
            LeavesAtCountdown(
                untilDeparture = departure.untilLeaves,
                untilShouldHaveLeft = departure.untilShouldHaveLeft,
            )
        }
    }
}

@Composable
private fun NextDeparture(
    departure: DashboardDeparture,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(start = 8.dp, end = 24.dp)) {
            LineBadge(
                line = departure.line,
                small = true,
            )
        }

        DepartureDetails(
            departure = departure,
            modifier = Modifier.weight(1.0f),
        )
        LeavesAtCountdown(
            untilDeparture = departure.untilLeaves,
            untilShouldHaveLeft = departure.untilShouldHaveLeft,
            large = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    DeparturesBoardTheme {
        Profile(
            profile = DashboardProfile(
                id = ProfileId.generate(),
                name = "Work",
                departures = listOf(
                    DashboardDeparture(
                        line = "134",
                        leavesAt = LocalTime(15, 1, 0, 0),
                        untilLeaves = 4.minutes,
                        untilShouldHaveLeft = 1.minutes,
                        delay = 3.minutes,
                        headsign = "Dvorce",
                    ),
                    DashboardDeparture(
                        line = "124",
                        leavesAt = LocalTime(15, 2, 0, 0),
                        untilLeaves = 2.minutes,
                        untilShouldHaveLeft = 0.minutes,
                        delay = 2.minutes,
                        headsign = "Zelený Pruh",
                    ),
                    DashboardDeparture(
                        line = "134",
                        leavesAt = LocalTime(15, 5, 0, 0),
                        untilLeaves = 7.minutes,
                        untilShouldHaveLeft = 5.minutes,
                        delay = 2.minutes,
                        headsign = "Dvorce",
                    ),
                )
            ),
            onClick = { },
            showNextDepartures = true,
        )
    }
}