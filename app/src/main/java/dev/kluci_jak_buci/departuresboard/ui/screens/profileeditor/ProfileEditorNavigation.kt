package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStation
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationResult
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.removeResult
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.resultKey
import dev.kluci_jak_buci.departuresboard.ui.screens.selectlines.SelectLines
import dev.kluci_jak_buci.departuresboard.ui.screens.selectlines.SelectLinesResult
import kotlinx.serialization.Serializable

@Serializable
object ProfileEditor {
}

fun NavGraphBuilder.profileEditor(
    navController: NavController,
    onBackArrowClick: () -> Unit,
) {
    composable<ProfileEditor> { backStackEntry ->
        val viewModel = hiltViewModel<ProfileEditorViewModel>()

        val stationResult by backStackEntry.savedStateHandle
            .getStateFlow<SearchStationResult?>(resultKey<SearchStationResult>(), null)
            .collectAsStateWithLifecycle()

        val linesResult by backStackEntry.savedStateHandle
            .getStateFlow<SelectLinesResult?>(resultKey<SelectLinesResult>(), null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(stationResult) {
            stationResult?.let {
                viewModel.onStationChanged(StationName(it.station))
                backStackEntry.savedStateHandle.removeResult<SearchStationResult>()
            }
        }

        LaunchedEffect(linesResult) {
            linesResult?.let { result ->
                viewModel.onLinesChanged(result.lines.map { it.toDomain() })
                backStackEntry.savedStateHandle.removeResult<SelectLinesResult>()
            }
        }

        val state by viewModel.uiState.collectAsState()
        ProfileEditorScreen(
            state = state,
            onNameChange = viewModel::onNameChange,
            onTimeFilterChange = viewModel::onTimeFilterChange,
            onAllDayChange = viewModel::onAllDayChange,
            onBackArrowClick = onBackArrowClick,
            onSaveClick = {},
            onSelectStationClick = {
                navController.navigate(SearchStation)
            },
            onSelectLinesClick = {
                state.selectedStation?.let {
                    navController.navigate(SelectLines(it.value))
                }
            }
        )
    }
}
