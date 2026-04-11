package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.core.os.BundleCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.setResult
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

val SelectedLineParcelableListType = object : NavType<List<SelectedLineParcelable>>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): List<SelectedLineParcelable>? {
        return BundleCompat.getParcelableArrayList(bundle, key, SelectedLineParcelable::class.java)
    }

    override fun parseValue(value: String): List<SelectedLineParcelable> {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: List<SelectedLineParcelable>) {
        bundle.putParcelableArrayList(key, if (value is ArrayList) value else ArrayList(value))
    }

    override fun serializeAsValue(value: List<SelectedLineParcelable>): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

@Serializable
data class SelectLines(
    val stationName: String,
    val initialSelectedLines: List<SelectedLineParcelable>
) {
    companion object {
        val typeMap = mapOf(
            typeOf<List<SelectedLineParcelable>>() to SelectedLineParcelableListType,
            typeOf<ArrayList<SelectedLineParcelable>>() to SelectedLineParcelableListType
        )
    }
}

fun NavGraphBuilder.selectLines(
    navController: NavController,
    onBackArrowClick: () -> Unit,
) {
    composable<SelectLines>(
        typeMap = SelectLines.typeMap
    ) {
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
