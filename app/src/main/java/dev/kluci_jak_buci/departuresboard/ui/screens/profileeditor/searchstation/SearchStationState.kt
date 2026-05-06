package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.searchstation

import dev.kluci_jak_buci.departuresboard.domain.model.StationName

data class SearchStationState(
    val searchText: String = "",
    val foundStations: List<StationName> = emptyList(),
)