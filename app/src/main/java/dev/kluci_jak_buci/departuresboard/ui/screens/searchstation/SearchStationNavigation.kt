package dev.kluci_jak_buci.departuresboard.ui.screens.searchstation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SearchStation {
}

fun NavGraphBuilder.searchStation(
    navController: NavController,
    onBackArrowClick: () -> Unit,
) {
    composable<SearchStation> {
        val viewModel = hiltViewModel<SearchStationViewModel>()
        val searchState by viewModel.uiState.collectAsStateWithLifecycle()

        SearchStationScreen(
            searchText = searchState.searchText,
            onSearchTextChange = viewModel::onSearchTextChange,
            foundStations = searchState.foundStations,
            onStationSelected = {
                navController.setResult(SearchStationResult(it.value))
            },
            onBackArrowClick = onBackArrowClick,
        )
    }
}

inline fun <reified T> resultKey(): String = T::class.qualifiedName!!

inline fun <reified T> NavController.setResult(value: T) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(resultKey<T>(), value)
    popBackStack()
}

inline fun <reified T> SavedStateHandle.getResult(): T? {
    return get(resultKey<T>())
}

inline fun <reified T> SavedStateHandle.removeResult() {
    remove<T>(resultKey<T>())
}