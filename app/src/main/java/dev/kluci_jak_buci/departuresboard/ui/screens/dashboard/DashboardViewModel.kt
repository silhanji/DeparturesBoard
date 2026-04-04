package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import dev.kluci_jak_buci.departuresboard.network.GolemioApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Departure(
    // Name of the route
    val route: String,
    // Minutes at which route leaves the platform
    val leavesAt: Number,
)

data class Station(
    // Id of station in Golemio API
    val id: String,
    // Official name of station
    val name: String,
    val platform: String,
    val nickname: String,
)

data class DashboardUiState(
    val stations: List<Station>,
    val departures: Map<String, List<Departure>>,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    profilesRepository: ProfilesRepository
) : ViewModel() {

    private val _departures = MutableStateFlow<Map<String, List<Departure>>>(
        mapOf()
    )

    val uiState: StateFlow<DashboardUiState> = combine(
        profilesRepository.getAll(),
        _departures
    ) { profiles, departures ->
        DashboardUiState(
            stations = profiles.map {
                Station(
                    id = it.selectedLines.first().platform.value,
                    name = it.name,
                    platform = "A",
                    nickname = it.name
                )
            },
            departures = departures
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(
            stations = emptyList(),
            departures = emptyMap()
        )
    )

    init {
        periodicallyRefreshDepartures()
    }

    private fun periodicallyRefreshDepartures() {
        viewModelScope.launch {
            while(true) {
                try {
                    val refreshedDepartures = mutableMapOf<String, List<Departure>>()
                    for(station in uiState.value.stations) {
                        val response = GolemioApi.retrofitService.getDepartures(
                            stationId =station.id,
                            limit = 15,
                            minutesBefore = 5
                        )
                        refreshedDepartures[station.id] =
                            response.flatMap { innerList -> innerList.map { departure ->
                                Departure(
                                    route = departure.route.shortName,
                                    leavesAt = departure.departure.minutes,
                                )
                            }}
                    }
                    _departures.update { refreshedDepartures }

                } catch(e: Exception) {
                    Log.e(null, "Error when refreshing departures", e)
                }

                // Refresh every 15s
                delay(15_000L)
            }
        }
    }
}