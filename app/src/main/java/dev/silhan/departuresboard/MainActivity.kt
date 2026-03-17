package dev.silhan.departuresboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NotListedLocation
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.silhan.departuresboard.ui.theme.DeparturesBoardTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeparturesBoardTheme {
                App(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    DashboardScreen(modifier)
}

@Composable
fun AddStationScreen(
    modifier: Modifier = Modifier,
    viewModel: AddStationViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    when(uiState) {
        is UiState.ChoosingStation -> ChooseStation(
            userInput = viewModel.userInput,
            userInputChanged = { newInput -> viewModel.updateUserInput(newInput) },
            onSearch = { viewModel.search() },
            stations = (uiState as UiState.ChoosingStation).stations,
            stationSelected = { stationId -> viewModel.chooseStation(stationId) },
            modifier = modifier,
        )
        is UiState.ShowingDepartures -> ShowDepartures(
            departures = (uiState as UiState.ShowingDepartures).departures,
            onChangeStation = { viewModel.reset() },
            modifier = modifier,
        )
        UiState.Error -> ShowError(
            onRetry = { viewModel.reset() },
            modifier,
        )
        UiState.Loading -> Loading(modifier)
    }
}

@Composable
fun ChooseStation(
    userInput: String,
    userInputChanged: (String) -> Unit,
    onSearch: () -> Unit,
    stations: List<AddStationStation>,
    stationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        Search(userInput, userInputChanged, onSearch)
        Stations(stations, stationSelected)
    }
}

@Composable
fun Search(
    userInput: String,
    userInputChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = userInputChanged,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Station name") }
        )
        Button(
            enabled = userInput.isNotEmpty(),
            onClick = { onSearch() },
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Text("Search")
        }
    }
}

@Composable
fun Stations(
    stations: List<AddStationStation>,
    stationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if(stations.isEmpty()) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.NotListedLocation,
            contentDescription = "No station found",
            modifier = modifier
                .padding(24.dp)
                .fillMaxWidth()
                .size(96.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    } else {
        Column(
            modifier = modifier.padding(24.dp),
        ) {
            for(station in stations) {
                val name = if(station.platform != null)
                    "${station.name} (${station.platform})"
                else
                    station.name

                Surface(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable( onClick = { stationSelected(station.id) }),
                ) {
                    Row {
                        Text(
                            text = name,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowDepartures(
    departures: List<AddStationDeparture>,
    onChangeStation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for(departure in departures) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = departure.routeName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "${departure.leavesAt} min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = onChangeStation,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Change station")
        }
    }
}

@Composable
fun Loading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Downloading,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(96.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            "Loading ...",
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun ShowError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            "Error :(",
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {
    DeparturesBoardTheme {
        App()
    }
}