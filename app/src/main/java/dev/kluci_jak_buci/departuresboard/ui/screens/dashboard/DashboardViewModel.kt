package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import dev.kluci_jak_buci.departuresboard.domain.repository.DeparturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration

/**
 * Number of milliseconds between periodic refresh of departures
 */
private const val DEPARTURES_REFRESH_INTERVAL = 15_000L

data class DashboardProfile(
    val id: ProfileId,
    val name: String,
    val departures: List<DashboardDeparture>,
)

data class DashboardDeparture(
    val line: String,
    val leavesAt: LocalTime,
    val untilLeaves: Duration,
    val untilShouldHaveLeft: Duration,
    val delay: Duration,
    val headsign: String,
)

data class DashboardUiState(
    val currentProfiles: List<DashboardProfile> = emptyList(),
    val savedProfiles: List<DashboardProfile> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    profilesRepository: ProfilesRepository,
    departuresRepository: DeparturesRepository,
) : ViewModel() {

    // Flow which does not produce any data, but serves as a trigger
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = profilesRepository.getAll()
        // Combining the two flows into one ensures that ui state is recomputed
        // either when profiles change in repository or when refresh trigger produces new data
        // (it gets triggered).
        //
        // This is achieved by using map function twice, on both flows.
        .flatMapLatest { profiles ->
            _refreshTrigger.map { profiles }
        }
        .map { profiles ->
            val now = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()


            val allDepartures = departuresRepository.get(profiles)

            // Convert profiles and departures into models consumed by UI
            val dashboardProfiles = profiles.map { profile ->
                val departures = allDepartures[profile].orEmpty()
                    .map { departure ->
                        DashboardDeparture(
                            line = departure.line.value,
                            leavesAt = departure.predicted.toLocalDateTime(timeZone).time,
                            untilShouldHaveLeft = departure.scheduled - now,
                            untilLeaves = departure.predicted - now,
                            delay = departure.delay,
                            headsign = departure.headsign,
                        )
                    }

                DashboardProfile(
                    id = profile.id,
                    name = profile.name,
                    departures = departures,
                )
            }

            // For debug only, current profiles should be determined by current time and location
            val currentConst = 2
            DashboardUiState(
                currentProfiles = dashboardProfiles.take(currentConst),
                savedProfiles = dashboardProfiles.drop(currentConst),
            )
        }
        // Do not wait until someone subscribes to the flow, instead
        // compute it always on background
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DashboardUiState(isLoading = true)
        )

    init {
        periodicallyRefreshDepartures()
    }

    /**
     * Launches a coroutine that periodically triggers _refreshTrigger and thus refreshes
     * the UI State
     */
    private fun periodicallyRefreshDepartures() {
        viewModelScope.launch {
            while(true) {
                delay(DEPARTURES_REFRESH_INTERVAL)
                _refreshTrigger.emit(Unit)
            }
        }
    }
}