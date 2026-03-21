package dev.silhan.departuresboard.ui.screens.searchstation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SearchStation

fun NavGraphBuilder.searchStation(
    onBackArrowClick: () -> Unit,
) {
    composable<SearchStation> {
        SearchStationScreen(
            onBackArrowClick = onBackArrowClick
        )
    }
}