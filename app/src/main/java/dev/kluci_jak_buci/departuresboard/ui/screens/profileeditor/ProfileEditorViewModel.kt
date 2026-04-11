package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.lifecycle.ViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.domain.model.VehicleFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime

//class InputState<T>(var T value) {
//    fun onValueChange(T newValue) {
//        value = newValue
//    }
//}

data class InputField<T>(
    val value: T,
    val errorMessage: String? = null // Alternatively, use @StringRes Int for localization
) {
    val isError: Boolean get() = errorMessage != null
}

data class ProfileEditorState(
    val name: InputField<String> = InputField(""),
    val timeFilter: InputField<TimeFilter> = InputField(TimeFilter(LocalTime(0, 0), LocalTime(23, 45))),
    val allDay: Boolean = false,
    val vehicleFilter: VehicleFilter? = null, // Assuming no validation needed here
    val selectedLines: InputField<List<SelectedLine>> = InputField(emptyList()),

    val selectedStation: StationName? = null,

    val isSaving: Boolean = false
) {
    // Derived state: The save button is only enabled if there are no errors
    // and required fields are filled.
    val isFormValid: Boolean = !name.isError &&
            name.value.isNotBlank() &&
            !timeFilter.isError &&
            !selectedLines.isError
}

class ProfileEditorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditorState())
    val uiState = _uiState.asStateFlow()

    // Expose direct, explicit functions
    fun onNameChange(newName: String) {
        // Validation logic here
        _uiState.update { it.copy(name = InputField(newName)) }
    }

    fun onTimeFilterChange(filter: TimeFilter) {
        _uiState.update { it.copy(timeFilter = InputField(filter)) }
    }

    fun onAllDayChange() {
        _uiState.update { it.copy(allDay = !it.allDay) }
    }

    fun onStationChanged(station: StationName) {
        _uiState.update { it.copy(selectedStation = station) }
    }

    fun onLinesChanged(lines: List<SelectedLine>) {
        _uiState.update { it.copy(selectedLines = InputField(lines)) }
    }

    fun saveProfile() {
        // Execute save logic
    }
}
