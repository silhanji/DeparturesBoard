package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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

        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(state.isSaveSuccessful) {
            if (state.isSaveSuccessful) {
                navController.popBackStack()
            }
        }

        ProfileEditorScreen(
            state = state,
            onNameChange = viewModel::onNameChange,
            onTimeFilterChange = viewModel::onTimeFilterChange,
            onAllDayChange = viewModel::onAllDayChange,
            onBackArrowClick = onBackArrowClick,
            onSaveClick = viewModel::saveProfile,
            onSelectStationClick = viewModel::onStationChanged,
            onLineClick = viewModel::toggleLine
        )
    }
}
