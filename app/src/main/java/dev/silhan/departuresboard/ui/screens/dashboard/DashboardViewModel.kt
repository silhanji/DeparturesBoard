package dev.silhan.departuresboard.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.silhan.departuresboard.network.GolemioApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val stations: List<Station> = listOf(
        Station(
            id = "U50Z6P",
            name = "Poliklinika Budějovická",
            platform = "L",
            nickname = "Budějovická (práce)"
        ),
        Station(
            id = "U361Z1P",
            name = "Malostranské náměstí",
            platform = "A",
            nickname = "Matfyz"
        ),
        Station(
            id = "U527Z101P",
            name = "Vyšehrad",
            platform = "M1",
            nickname = "Vyšehrad (do centra)"
        ),
    ),
    val departures: Map<String, List<Departure>> = mapOf(),
)

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        periodicallyRefreshDepartures()
    }

    private fun periodicallyRefreshDepartures() {
        viewModelScope.launch {
            while(true) {
                try {
                    val refreshedDepartures = mutableMapOf<String, List<Departure>>()
                    for(station in _uiState.value.stations) {
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
                    _uiState.update { currentState ->
                        currentState.copy(
                            departures = refreshedDepartures
                        )
                    }

                } catch(e: Exception) {
                    Log.e(null, "Error when refreshing departures", e)
                }

                // Refresh every 15s
                delay(15_000L)
            }
        }
    }
}