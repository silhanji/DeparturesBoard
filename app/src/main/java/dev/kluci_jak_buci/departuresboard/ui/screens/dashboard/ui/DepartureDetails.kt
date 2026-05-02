package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.DashboardDeparture
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes


@Composable
fun DepartureDetails(
    departure: DashboardDeparture,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = departure.headsign.uppercase(),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
        )
        DepartureTime(
            departure = departure.leavesAt,
        )
    }
}

@Composable
private fun DepartureTime(
    departure: LocalTime,
    modifier: Modifier = Modifier
) {
    val hour = departure.hour.toString().padStart(2, '0')
    val minute = departure.minute.toString().padStart(2, '0')

    Row(modifier) {
        Text(
            text = "${hour}:${minute}",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DepartureDetailsPreview() {
    DeparturesBoardTheme {
        DepartureDetails(
            departure = DashboardDeparture(
                line = "C",
                leavesAt = LocalTime(15, 0, 0, 0),
                untilLeaves = 0.minutes,
                untilShouldHaveLeft = 0.minutes,
                delay = 0.minutes,
                headsign = "Haje",
            ),
        )
    }
}