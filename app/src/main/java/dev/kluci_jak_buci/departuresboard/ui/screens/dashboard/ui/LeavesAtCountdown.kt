package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


@Composable
fun LeavesAtCountdown(
    untilDeparture: Duration,
    untilShouldHaveLeft: Duration,
    modifier: Modifier = Modifier,
    large: Boolean = true,
) {
    if(untilDeparture > untilShouldHaveLeft) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
        ) {
            Countdown(
                duration = untilDeparture,
                large = large,
                isDelayed = true,
                isValid = true,
            )
            Countdown(
                duration = untilShouldHaveLeft,
                large = false,
                isValid = false,
                isDelayed = false,
            )
        }
    }
    else {
        Countdown(
            duration = untilDeparture,
            large = large,
            isDelayed = false,
            isValid = true,
            modifier = modifier,
        )
    }
}


@Composable
private fun Countdown(
    duration: Duration,
    modifier: Modifier = Modifier,
    large: Boolean = true,
    isValid: Boolean = true,
    isDelayed: Boolean = false,
) {
    var style = if(large)
        MaterialTheme.typography.headlineSmall
    else
        MaterialTheme.typography.bodyMedium

    var minutesStyle = MaterialTheme.typography.bodyMedium

    if(isDelayed) {
        style = style.copy(
            color = MaterialTheme.colorScheme.error,
        )
        minutesStyle = minutesStyle.copy(
            color = MaterialTheme.colorScheme.error,
        )
    }

    if(! isValid) {
        style = style.copy(
            textDecoration = TextDecoration.LineThrough,
        )
        minutesStyle = minutesStyle.copy(
            textDecoration = TextDecoration.LineThrough,
        )
    }


    if(duration.inWholeMinutes == 0L) {
        Text(
            text = "Nyni",
            fontWeight = FontWeight.Bold,
            style = style,
            modifier = modifier,
        )
    } else {
        Row(modifier = modifier) {
            Text(
                text = "${duration.inWholeMinutes}",
                fontWeight = FontWeight.Bold,
                style = style,
                modifier = Modifier.alignByBaseline(),
            )
            Text(
                text = " min",
                fontWeight = FontWeight.Bold,
                style = minutesStyle,
                modifier = Modifier.alignByBaseline(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeavesAtCountdownPreview() {
    DeparturesBoardTheme {
        LeavesAtCountdown(
            untilDeparture = 2.minutes,
            untilShouldHaveLeft = 1.minutes,
            large = true,
        )
    }
}