package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui.NoProfiles
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui.Profiles
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes

@Composable
fun DashboardScreen(
    onAddDepartureClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Screen(
        uiState,
        onAddDepartureClick,
        onSettingsClick,
        onProfileClick,
        modifier,
    )
}

@Composable
private fun Screen(
    uiState: DashboardUiState,
    onAddDepartureClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopBar(onSettingsClick) },
        floatingActionButton = {
            if(uiState.savedProfiles.isNotEmpty()) {
                Fab(
                    onclick = { onAddDepartureClick() }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        if(uiState.isLoading)
            return@Scaffold

        if(uiState.currentProfiles.isEmpty() && uiState.savedProfiles.isEmpty()) {
            NoProfiles(
                onAddDepartureClick = onAddDepartureClick,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Profiles(
                currentProfiles = uiState.currentProfiles,
                savedProfiles = uiState.savedProfiles,
                onProfileClick = onProfileClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    TopAppBar(
        title = { Text("Odjezdy") },
        actions = {
            IconButton(
                onClick = { onSettingsClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    )
}

@Composable
private fun Fab(
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

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DashboardScreenPreview() {
    DeparturesBoardTheme {
        Screen(
            uiState = DashboardUiState(
                currentProfiles = listOf(DashboardProfile(
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
                )),
                savedProfiles = listOf(
                    DashboardProfile(
                        id = ProfileId.generate(),
                        name = "Work",
                        departures = listOf()
                    ),
                    DashboardProfile(
                        id = ProfileId.generate(),
                        name = "Home",
                        departures = listOf(
                            DashboardDeparture(
                                line = "C",
                                leavesAt = LocalTime(15, 0, 0, 0),
                                untilLeaves = 0.minutes,
                                untilShouldHaveLeft = 0.minutes,
                                delay = 0.minutes,
                                headsign = "Haje",
                            ),
                            DashboardDeparture(
                                line = "C",
                                leavesAt = LocalTime(15, 2, 0, 0),
                                untilLeaves = 2.minutes,
                                untilShouldHaveLeft = 0.minutes,
                                delay = 2.minutes,
                                headsign = "Haje",
                            ),
                            DashboardDeparture(
                                line = "C",
                                leavesAt = LocalTime(15, 5, 0, 0),
                                untilLeaves = 5.minutes,
                                untilShouldHaveLeft = 7.minutes,
                                delay = (-2).minutes,
                                headsign = "Kacerov",
                            ),
                        )
                    )
                ),
                isLoading = false,
            ),
            onAddDepartureClick = { },
            onSettingsClick = { },
            onProfileClick = { },
        )
    }
}
