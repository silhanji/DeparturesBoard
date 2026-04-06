package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject
import kotlin.time.Clock

data class DashboardProfile(
    val name: String,
    val departures: List<DashboardDeparture>,
)

data class DashboardDeparture(
    val line: String,
    val minutesUntil: Int,
)

data class DashboardUiState(
    val profiles: List<DashboardProfile> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    profilesRepository: ProfilesRepository,
    departuresRepository: DeparturesRepository,
) : ViewModel() {

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = profilesRepository.getAll()
        .flatMapLatest { profiles ->
            _refreshTrigger.map { profiles }
        }
        .map { profiles ->
            val now = Clock.System.now();

            val dashboardProfiles = profiles.map { profile ->
                val departures = try {
                    departuresRepository.get(profile)
                        .map { departure ->
                            DashboardDeparture(
                                line = departure.line.value,
                                minutesUntil = (departure.predicted - now).inWholeMinutes.toInt()
                            )
                        }
                } catch(e: Exception) {
                    Log.e("DashboardVM", "Failed to fetch departures for ${profile.name}", e)
                    emptyList()
                }

                DashboardProfile(
                    name = profile.name,
                    departures = departures
                )
            }

            DashboardUiState(
                profiles = dashboardProfiles,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState(isLoading = true)
        )

    init {
        periodicallyRefreshDepartures()
    }

    private fun periodicallyRefreshDepartures() {
        viewModelScope.launch {
            while(true) {
                // Refresh every 15s
                delay(15_000L)
                _refreshTrigger.emit(Unit)
            }
        }
    }
}