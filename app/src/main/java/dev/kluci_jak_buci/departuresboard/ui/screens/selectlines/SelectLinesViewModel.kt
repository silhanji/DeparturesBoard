package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.repository.StationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectLinesState(val station: Station, initialSelectedLines: List<SelectedLine> = emptyList()) {

    val selectedLines = mutableStateListOf<Line>().apply {
        val allLines = station.platforms.flatMap { it.lines }.distinct()
        val initialLines = initialSelectedLines.mapNotNull { selected ->
            allLines.find { it.name == selected.line }
        }
        addAll(initialLines)
    }

    val lines: List<Line>
        get() = station.platforms
            .flatMap { it.lines }
            .distinct()

    /**
     * Retrieves all platform - line pairs that are selected by user.
     */
    fun getSelectedProfileLines(): List<SelectedLine> {
        return station.platforms.flatMap { platform ->
             selectedLines
                 .filter { platform.lines.contains(it)  }
                 .map { SelectedLine(it.name, platform.id) }
        }
    }

    fun selectLine(line: Line) {
        if (selectedLines.contains(line)) {
            selectedLines.remove(line)
        } else {
            selectedLines.add(line)
        }
    }
}

