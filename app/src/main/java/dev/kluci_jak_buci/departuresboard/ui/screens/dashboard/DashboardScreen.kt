package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun DashboardScreen(
    onAddDepartureClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            DashboardFab(
                onclick = { onAddDepartureClick() }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(uiState.profiles) { item ->
                Text(item.name)
                item.departures.forEach { departure ->
                    Departure(departure)
                }
            }
        }
    }
}

@Composable
fun Departure(
    departure: DashboardDeparture,
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
                    text = departure.line,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
                Text(
                    text = formatDepartureText(departure.minutesUntil),
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
            onAddDepartureClick = { }
        )
    }
}
