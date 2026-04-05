package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.NoTransfer
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun DashboardScreen(
    onAddDepartureClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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
            Column(modifier = modifier.padding(innerPadding)) {
                if(uiState.currentProfiles.isNotEmpty()) {
                    CurrentProfiles(
                        profiles = uiState.currentProfiles,
                        onProfileClick = onProfileClick,
                    )
                }

                if(uiState.savedProfiles.isNotEmpty()) {
                    SavedProfiles(
                        profiles = uiState.savedProfiles,
                        onProfileClick = onProfileClick,
                    )
                }
            }
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
        title = { },
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

enum class SectionType {
    CURRENT_PROFILES,
    SAVED_PROFILES,
}

@Composable
private fun Section(
    section: SectionType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { },
) {
    Column(modifier = modifier.padding(top = 32.dp)) {
        SectionLabel(section)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SectionLabel(
    section: SectionType,
    modifier: Modifier = Modifier,
) {
    val icon = if(section == SectionType.CURRENT_PROFILES)
        Icons.Default.PinDrop
    else
        Icons.Default.Bookmark

    val text = if(section == SectionType.CURRENT_PROFILES)
        "Aktualni odjezdy"
    else
        "Ulozene odjezdy"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun CurrentProfiles(
    profiles: List<DashboardProfile>,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier
) {
    Section(SectionType.CURRENT_PROFILES) {
        LazyColumn(modifier = modifier) {
            items(profiles) { item ->
                Profile(
                    profile = item,
                    onClick = { onProfileClick(item.id) },
                    showNextDepartures = true,
                )
            }
        }
    }
}

@Composable
private fun SavedProfiles(
    profiles: List<DashboardProfile>,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Section(SectionType.SAVED_PROFILES) {
        LazyColumn(modifier = modifier) {
            items(profiles) { item ->
                Profile(
                    profile = item,
                    onClick = { onProfileClick(item.id) },
                    showNextDepartures = false,
                )
            }
        }
    }
}

@Composable
private fun Profile(
    profile: DashboardProfile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showNextDepartures: Boolean = true,
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
                for(i in 1 until 3.coerceAtMost(profile.departures.size)) {
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
                untilDeparture = departure.untilLeaves
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
            largeMinutes = false,
        )
    }
}

@Composable
private fun DepartureDetails(
    departure: DashboardDeparture,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Smer: ${departure.headsign}",
            style = MaterialTheme.typography.bodyMedium,
        )
        DepartureTime(
            departure = departure.leavesAt,
            delay = departure.delay,
        )
    }
}

@Composable
private fun LineBadge(
    line: String?,
    modifier: Modifier = Modifier,
    small: Boolean = false,
) {
    val size = if(small) 48.dp else 64.dp
    val textStyle = if(small)
        MaterialTheme.typography.bodySmall
    else
        MaterialTheme.typography.headlineSmall


    Surface(
        shape = RoundedCornerShape(1000.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .width(size)
            .aspectRatio(1.0f),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if(line != null) {
                Text(
                    text = line,
                    style = textStyle,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.NoTransfer,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun DepartureTime(
    departure: LocalTime,
    delay: Duration,
    modifier: Modifier = Modifier
) {
    val hour = departure.hour.toString().padStart(2, '0')
    val minute = departure.minute.toString().padStart(2, '0')

    Row(modifier) {
        Text(
            text = "${hour}:${minute}",
            style = MaterialTheme.typography.bodyMedium,
        )

        if(delay.inWholeMinutes > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "(+${delay.inWholeMinutes}min)",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun LeavesAtCountdown(
    untilDeparture: Duration,
    modifier: Modifier = Modifier,
    largeMinutes: Boolean = true,
) {
    val style = if(largeMinutes)
        MaterialTheme.typography.headlineSmall
    else
        MaterialTheme.typography.bodyMedium

    if(untilDeparture.inWholeMinutes == 0L) {
        Text(
            text = "Teď",
            fontWeight = FontWeight.Bold,
            style = style,
            modifier = modifier,
        )
    } else {
        Row(modifier = modifier) {
            Text(
                text = "${untilDeparture.inWholeMinutes}",
                fontWeight = FontWeight.Bold,
                style = style,
                modifier = Modifier.alignByBaseline(),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "min",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alignByBaseline(),
            )
        }
    }
}

@Composable
private fun NoProfiles(
    onAddDepartureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.NoTransfer,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = "Nemáte nastaveny žádné odjezdy",
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 32.dp,
            )
        )
        Button(
            onClick = onAddDepartureClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Přidat odjezd")
            }
        }
    }
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
                            untilLeaves = 2.minutes,
                            delay = (-2).minutes,
                            headsign = "Dvorce",
                        ),
                        DashboardDeparture(
                            line = "124",
                            leavesAt = LocalTime(15, 2, 0, 0),
                            untilLeaves = 2.minutes,
                            delay = 2.minutes,
                            headsign = "Zelený Pruh",
                        ),
                        DashboardDeparture(
                            line = "134",
                            leavesAt = LocalTime(15, 5, 0, 0),
                            untilLeaves = 5.minutes,
                            delay = (-2).minutes,
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
                                delay = 0.minutes,
                                headsign = "Haje",
                            ),
                            DashboardDeparture(
                                line = "C",
                                leavesAt = LocalTime(15, 2, 0, 0),
                                untilLeaves = 2.minutes,
                                delay = 2.minutes,
                                headsign = "Haje",
                            ),
                            DashboardDeparture(
                                line = "C",
                                leavesAt = LocalTime(15, 5, 0, 0),
                                untilLeaves = 5.minutes,
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
