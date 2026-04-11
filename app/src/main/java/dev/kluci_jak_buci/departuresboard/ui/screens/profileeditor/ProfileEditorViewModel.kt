package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.domain.model.VehicleFilter
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

data class ProfileEditorState(
    val name: InputField<String> = InputField(""),
    val timeFilter: InputField<TimeFilter> = InputField(TimeFilter(LocalTime(0, 0), LocalTime(23, 45))),
    val allDay: Boolean = false,
    val vehicleFilter: VehicleFilter? = null,
    val selectedLines: InputField<List<SelectedLine>> = InputField(emptyList()),
    val selectedStation: StationName? = null,
    val isSaving: Boolean = false,
    val isSaveSuccessful: Boolean = false
) {
    val isFormValid: Boolean = !name.isError &&
            name.value.isNotBlank() &&
            !timeFilter.isError &&
            selectedLines.value.isNotEmpty()
}

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditorState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
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

    @OptIn(ExperimentalUuidApi::class)
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
                // In a real app, handle error state here
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
            vehicleFilter = vehicleFilter
        )
    }
}
