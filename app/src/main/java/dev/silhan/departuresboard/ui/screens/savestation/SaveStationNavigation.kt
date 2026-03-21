package dev.silhan.departuresboard.ui.screens.savestation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SaveStation

fun NavGraphBuilder.saveStation(
    onBackArrowClick: () -> Unit,
) {
    composable<SaveStation> {
        SaveStationScreen(
            onBackArrowClick = onBackArrowClick
        )
    }
}