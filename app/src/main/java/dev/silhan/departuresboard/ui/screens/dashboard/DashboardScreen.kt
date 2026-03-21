package dev.silhan.departuresboard.ui.screens.dashboard

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.silhan.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            DashboardFab(
                onclick = { }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(uiState.stations) { item ->
                Station(
                    item,
                    departures = uiState.departures[item.id]
                )
            }
        }
    }
}

@Composable
fun Station(
    station: Station,
    departures: List<Departure>?,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(size = 8.dp),
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp),
    ) {
        Column {
            Row(
                modifier = modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = station.nickname,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = station.name,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if(departures !== null && departures.any { dep -> dep.leavesAt.toInt() >= 0 }) {
                    Text(
                        text = formatDepartureText(departures.first {dep -> dep.leavesAt.toInt() >= 0}.leavesAt),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(
                        top = 0.dp, bottom = 16.dp, start = 0.dp, end = 0.dp
                    )
                    .horizontalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                if(departures !== null) {
                    for(departure in departures) {
                        if(departure.leavesAt.toInt() < 0) {
                            continue
                        }
                        Departure(departure)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun Departure(
    departure: Departure,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(64.dp)
            .width(64.dp),
        tonalElevation = 80.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = departure.route,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
                Text(
                    text = formatDepartureText(departure.leavesAt),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DashboardFab(
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = { onclick() },
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

fun formatDepartureText(leavesAt: Number): String {
    return if(leavesAt.toInt() == 0)
        "Teď"
    else
        "${leavesAt}min"
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DashboardScreenPreview() {
    DeparturesBoardTheme {
        DashboardScreen(

        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun StationPreview() {
    DeparturesBoardTheme {
        Station(
            station = Station(
                id = "XXX",
                name = "Poliklinika Budejovicka",
                platform = "P",
                nickname = "Budejovicka (prace)"
            ),
            departures = listOf(
                Departure(
                    route = "134",
                    leavesAt = -1
                ),
                Departure(
                    route = "124",
                    leavesAt = 0
                ),
                Departure(
                    route = "170",
                    leavesAt = 1
                ),
                Departure(
                    route = "124",
                    leavesAt = 3
                ),
                Departure(
                    route = "134",
                    leavesAt = 4
                ),
                Departure(
                    route = "170",
                    leavesAt = 5
                ),
                Departure(
                    route = "134",
                    leavesAt = 6
                ),
            )
        )
    }
}