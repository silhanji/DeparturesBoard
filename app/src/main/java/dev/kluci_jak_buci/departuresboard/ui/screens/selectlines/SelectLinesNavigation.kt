package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.setResult
import kotlinx.serialization.Serializable

@Serializable
data class SelectLines(
    val stationName: String
)

fun NavGraphBuilder.selectLines(
    navController: NavController,
    onBackArrowClick: () -> Unit,
) {
    composable<SelectLines> {
        val viewModel = hiltViewModel<SelectLinesViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        state?.let { selectLinesState ->
            SelectLinesScreen(
                selectLinesState = selectLinesState,
                onBackArrowClick = onBackArrowClick,
                onConfirmClick = {
                    val result = SelectLinesResult(
                        lines = selectLinesState.getSelectedProfileLines().map { it.toParcelable() }
                    )
                    navController.setResult(result)
                }
            )
        }
    }
}
