package dev.silhan.departuresboard.ui.screens.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Dashboard

fun NavGraphBuilder.dashboard(
    onAddDepartureClick: () -> Unit,
) {
    composable<Dashboard> {
        DashboardScreen(
            onAddDepartureClick = onAddDepartureClick
        )
    }
}