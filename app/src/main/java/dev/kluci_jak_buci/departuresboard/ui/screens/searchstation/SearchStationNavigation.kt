package dev.kluci_jak_buci.departuresboard.ui.screens.searchstation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SearchStation {
}

fun NavGraphBuilder.searchStation(
    onBackArrowClick: () -> Unit,
) {
    composable<SearchStation> {
        val viewModel = hiltViewModel<SearchStationViewModel>()
        val searchState by viewModel.uiState.collectAsStateWithLifecycle()

        SearchStationScreen(
            searchText = searchState.searchText,
            onSearchTextChange = viewModel::onSearchTextChange,
            foundStations = searchState.foundStations,
            onStationSelected = { },
            onBackArrowClick = onBackArrowClick,
        )
    }
}

