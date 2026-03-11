package dev.silhan.departuresboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.silhan.departuresboard.network.GolemioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Station(
    val id: String,
    val name: String,
    val platform: String?,
)

data class Departure(
    val routeName: String,
    val leavesAt: Number,
)

sealed interface UiState {
    data class ChoosingStation(
        val stations: List<Station> = listOf()
    ): UiState
    data class ShowingDepartures(
        val departures: List<Departure>
    ): UiState
    object Loading: UiState
    object Error: UiState
}

class AddStationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.ChoosingStation());
    val uiState: StateFlow<UiState> = _uiState.asStateFlow();

    var userInput by mutableStateOf("")
        private set

    fun updateUserInput(newInput: String) {
        userInput = newInput;
    }

    fun search() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val response = GolemioApi.retrofitService.getStops(userInput)
                _uiState.update {
                    UiState.ChoosingStation(
                        stations = response.features.map { feature ->
                            Station(
                                id = feature.properties.stopId,
                                name = feature.properties.stopName,
                                platform = feature.properties.platformCode
                            )
                        },
                    )
                }
            } catch(e: Exception) {
                Log.e(null, "Error when searching for stop", e)
                _uiState.update { UiState.Error }
            }

        }
    }

    fun chooseStation(id: String) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            try {
                val response = GolemioApi.retrofitService.getDepartures(id)
                _uiState.update {
                    UiState.ShowingDepartures(
                        departures = response.flatMap { innerList -> innerList.map { departure ->
                            Departure(
                                routeName = departure.route.shortName,
                                leavesAt = departure.departure.minutes
                            )
                        }}
                    )
                }
            } catch(e: Exception) {
                Log.e(null, "Error when searching for departures", e)
                _uiState.update { UiState.Error }
            }
        }
    }

    fun reset() {
        _uiState.update { UiState.ChoosingStation() }
    }
}

