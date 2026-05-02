package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.DashboardDeparture
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.DashboardProfile
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes

@Composable
fun Profiles(
    currentProfiles: List<DashboardProfile>,
    savedProfiles: List<DashboardProfile>,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if(currentProfiles.isNotEmpty()) {
            CurrentProfiles(
                profiles = currentProfiles,
                onProfileClick = onProfileClick,
            )
        }

        if(savedProfiles.isNotEmpty()) {
            SavedProfiles(
                profiles = savedProfiles,
                onProfileClick = onProfileClick,
            )
        }
    }
}

@Composable
private fun CurrentProfiles(
    profiles: List<DashboardProfile>,
    onProfileClick: (id: ProfileId) -> Unit,
    modifier: Modifier = Modifier
) {
    Section(Section.CurrentProfiles) {
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
    Section(Section.SavedProfiles) {
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

@Preview(showBackground = true)
@Composable
fun ProfilesPreview() {
    DeparturesBoardTheme {
        Profiles(
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
            onProfileClick = { }
        )
    }
}