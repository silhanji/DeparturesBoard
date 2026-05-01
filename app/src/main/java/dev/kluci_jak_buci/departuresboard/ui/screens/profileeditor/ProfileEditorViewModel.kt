package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import dev.kluci_jak_buci.departuresboard.domain.repository.StationsRepository
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.searchstation.SearchStationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

data class InputField<T>(
    val value: T,
    val errorMessage: String? = null
) {
    val isError: Boolean get() = errorMessage != null
}

val INIT_TIME_FILTER = TimeFilter(LocalTime(0, 0), LocalTime(23, 59))

sealed class EditorScreen {
    data object General : EditorScreen()
    data object SelectLines : EditorScreen()
    data class SearchStation(val state: SearchStationState) : EditorScreen()
}

data class ProfileEditorState(
    val name: InputField<String> = InputField(""),
    val timeFilter: InputField<TimeFilter> = InputField(INIT_TIME_FILTER),
    val allDay: Boolean = true,
    /**
     * Represents a list of selected lines for selected station.
     *
     * When station changes, the list is cleared.
     */
    val selectedLines: InputField<List<SelectedLine>> = InputField(emptyList()),
    val selectedStation: Station? = null,
    val openScreen: EditorScreen = EditorScreen.General,
    val isSaving: Boolean = false,
    val isSaveSuccessful: Boolean = false
) {
    val isFormValid: Boolean =
        !name.isError &&
        name.value.isNotBlank() &&
        !timeFilter.isError &&
        selectedLines.value.isNotEmpty()
}
const val SEARCH_DEBOUNCE: Long = 300

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditorState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState
                .filter { it.openScreen is EditorScreen.SearchStation }
                .map{ it.openScreen as EditorScreen.SearchStation }
                .map { it.state.searchText }
                .debounce(SEARCH_DEBOUNCE)
                .distinctUntilChanged()
                .collectLatest { searchText ->
                    val foundStations = stationsRepository.search(searchText)
                    updateSearchStationState {
                        it.copy(foundStations = foundStations, searchText =  searchText)
                    }
                }
        }
    }

    fun searchForStation(text: String) {
        updateSearchStationState {
            it.copy(searchText = text)
        }
    }

    fun setName(newName: String) {
        _uiState.update { it.copy(name = InputField(newName)) }
    }

    fun setTimeFilter(filter: TimeFilter) {
        _uiState.update { it.copy(timeFilter = InputField(filter)) }
    }

    fun toggleAllDay() {
        _uiState.update { it.copy(allDay = !it.allDay) }
    }

    /**
     * Selects a line for the given station to be added to profile draft.
     * If already selected, the line is deselected instead.
     */
    fun selectLine(line: Line) {
        val station = _uiState.value.selectedStation ?: return

        viewModelScope.launch {
            // get station platform for the line
            val platform = station.platforms
                .find { platform -> platform.lines.any { it == line } } ?: return@launch

            val newSelectedLine = SelectedLine(line.name, platform.id)

            _uiState.update { currentState ->
                val currentList = currentState.selectedLines.value

                // add to selected lines, if already selected, deselect
                val lineAlreadySelected = currentList.any { it == newSelectedLine }
                val newSelectedLines = if (lineAlreadySelected) {
                    currentList.filterNot { it == newSelectedLine }
                } else {
                    currentList + newSelectedLine
                }
                currentState.copy(selectedLines = InputField(newSelectedLines))
            }
        }
    }

    /**
     * Push new screen on top of existing screens.
     * Cannot be used to go back to General screen (or any other previous screen).
     */
    fun pushScreen(screen: EditorScreen) {
        if (screen == EditorScreen.General) {
            throw IllegalArgumentException("Cannot open screen General, " +
                    "General screen can be opened only by closing current screen")
        }

        _uiState.update {
            it.copy(openScreen = screen)
        }
    }

    /**
     * Pop current open screen, currently only single open screen is supported.
     * so the state returns to general screen.
     */
    fun popScreen() {
        _uiState.update {
            it.copy(openScreen = EditorScreen.General)
        }
    }

    fun selectStation(stationName: StationName) {
        viewModelScope.launch {
            val station = stationsRepository.get(stationName)
            _uiState.update { 
                it.copy(selectedStation = station, selectedLines = InputField(emptyList()))
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)
    fun saveProfile() {
        val currentState = _uiState.value
        if (!currentState.isFormValid || currentState.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val profile = currentState.toDomainProfile()
                profilesRepository.create(profile)
                _uiState.update { it.copy(isSaveSuccessful = true, isSaving = false) }
            } catch (e: Exception) {
                Log.e(ProfileEditorViewModel::class.simpleName, "Failed to create profile", e)
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun ProfileEditorState.toDomainProfile(): Profile {
        return Profile(
            id = ProfileId.generate(),
            name = name.value,
            selectedLines = selectedLines.value,
            timeFilter = if (allDay) null else timeFilter.value,
            vehicleFilter = null
        )
    }

    private fun updateSearchStationState(
        transform: (SearchStationState) -> SearchStationState
    ) {
        _uiState.update { state ->
            val screen = state.openScreen
            if (screen is EditorScreen.SearchStation) {
                state.copy(
                    openScreen = screen.copy(
                        state = transform(screen.state)
                    )
                )
            } else {
                Log.e(
                    ProfileEditorViewModel::class.simpleName,
                    "Updating station when not in search screen, open screen $screen"
                )
                state
            }
        }
    }
}