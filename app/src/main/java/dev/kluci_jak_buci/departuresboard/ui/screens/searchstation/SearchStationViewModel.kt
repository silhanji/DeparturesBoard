package dev.kluci_jak_buci.departuresboard.ui.screens.searchstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.repository.StationsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchStationUiState(
    val searchText: String = "",
    val foundStations: List<StationName> = emptyList(),
)

const val SEARCH_DEBOUNCE: Long = 300

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchStationViewModel @Inject constructor(
    private val _stationsRepository: StationsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchStationUiState())

    val uiState: StateFlow<SearchStationUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState
                .map{ it.searchText }
                .debounce(SEARCH_DEBOUNCE)
                .distinctUntilChanged()
                .collectLatest { searchText ->
                    val foundStations = _stationsRepository.search(searchText)
                    _uiState.update {
                        it.copy(
                            searchText = searchText,
                            foundStations = foundStations
                        )
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update {
            it.copy(searchText = text)
        }
    }
}