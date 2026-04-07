package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import androidx.compose.runtime.mutableStateListOf
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.Station

class SelectLinesState(val station: Station, initialSelectedLines: List<Line> = emptyList()) {

    val selectedLines = mutableStateListOf<Line>().apply {
        addAll(initialSelectedLines)
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