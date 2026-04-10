package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationScreen
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationViewModel
import kotlinx.serialization.Serializable

@Serializable
object ProfileEditor {
}

fun NavGraphBuilder.profileEditor(
    onBackArrowClick: () -> Unit,
) {
    composable<ProfileEditor> {
    }
}