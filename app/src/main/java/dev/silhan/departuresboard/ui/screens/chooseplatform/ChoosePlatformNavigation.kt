package dev.silhan.departuresboard.ui.screens.chooseplatform

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class ChoosePlatform(
    val stationId: String
)

fun NavGraphBuilder.choosePlatform(
    onBackArrowClick: () -> Unit,
) {
    composable<ChoosePlatform> { backStackEntry ->
        val route: ChoosePlatform = backStackEntry.toRoute()

        ChoosePlatformScreen(
            onBackArrowClick = onBackArrowClick,
        )
    }
}