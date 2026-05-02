package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import kotlinx.serialization.Serializable

@Serializable
object Dashboard

fun NavGraphBuilder.dashboard(
    onAddDepartureClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: (id: ProfileId) -> Unit,
) {
    composable<Dashboard> {
        DashboardScreen(
            onAddDepartureClick = onAddDepartureClick,
            onSettingsClick = onSettingsClick,
            onProfileClick = onProfileClick,
        )
    }
}