@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Tram
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.ui.components.BottomSheetHeader
import dev.kluci_jak_buci.departuresboard.ui.components.MultiLineClickableField
import dev.kluci_jak_buci.departuresboard.ui.components.Field
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.ProfileEditorState
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationStandalone
import dev.kluci_jak_buci.departuresboard.ui.screens.selectlines.SelectLines
import kotlinx.coroutines.launch

@Composable
fun LinesSection(
    state: ProfileEditorState,
    onSelectStationClick: (StationName) -> Unit,
    onLineClick: (Line) -> Unit = {},
) {
    var showLinesBottomSheet by remember { mutableStateOf(false) }
    var showStationBottomSheet by remember { mutableStateOf(false) }

    Section(
        name = stringResource(R.string.lines)
    ) {
        StationOutlinedField(
            stationName = state.selectedStation,
            onClick = { showStationBottomSheet = true }
        )

        LinesOutlinedField(
            selectedLines = state.resolvedLines.filter { line ->
                state.selectedLines.value.any { it.line == line.name }
            },
            onClick = { showLinesBottomSheet = true },
            enabled = state.selectedStation != null
        )
    }

    SearchStationBottomSheet(
        onSelectStationClick = onSelectStationClick,
        showStationBottomSheet = showStationBottomSheet,
        showStationBottomSheetChange = { showStationBottomSheet = it }
    )

    SelectLinesBottomSheet(
        state = state,
        onLineClick = onLineClick,
        showLinesBottomSheet = showLinesBottomSheet,
        onShowLinesBottomSheetChange = { showLinesBottomSheet = it }
    )
}

@Composable
private fun SearchStationBottomSheet(
    onSelectStationClick: (StationName) -> Unit,
    showStationBottomSheet: Boolean,
    showStationBottomSheetChange: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val searchStationSheetState = rememberModalBottomSheetState()

    if (showStationBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showStationBottomSheetChange(false) },
            sheetState = searchStationSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            BottomSheetHeader(
                title = stringResource(R.string.choose_station),
                modifier = Modifier.Companion.fillMaxWidth(),
                onBackClick = {
                    scope.launch { searchStationSheetState.hide() }.invokeOnCompletion {
                        if (!searchStationSheetState.isVisible) {
                            showStationBottomSheetChange(false)
                        }
                    }
                }
            )
            SearchStationStandalone(
                onStationSelected = { stationName ->
                    scope.launch { searchStationSheetState.hide() }.invokeOnCompletion {
                        if (!searchStationSheetState.isVisible) {
                            showStationBottomSheetChange(false)
                            onSelectStationClick(stationName)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun SelectLinesBottomSheet(
    state: ProfileEditorState,
    onLineClick: (Line) -> Unit = {},
    showLinesBottomSheet: Boolean,
    onShowLinesBottomSheetChange: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val selectLinesSheetState = rememberModalBottomSheetState()

    if (showLinesBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onShowLinesBottomSheetChange(false) },
            sheetState = selectLinesSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            BottomSheetHeader(
                title = stringResource(R.string.select_line),
                modifier = Modifier.Companion.fillMaxWidth(),
                onConfirmClick = {
                    scope.launch { selectLinesSheetState.hide() }.invokeOnCompletion {
                        if (!selectLinesSheetState.isVisible) {
                            onShowLinesBottomSheetChange(false)
                        }
                    }
                }
            )
            SelectLines(
                lines = state.resolvedLines,
                selectedLines = state.resolvedLines.filter { line ->
                    state.selectedLines.value.any { it.line == line.name }
                },
                onLineClick = onLineClick,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun StationOutlinedField(
    stationName: StationName?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textValue = stationName?.value ?: ""

    Box(modifier = modifier) {
        Field(
            value = textValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.station)) },
            placeholder = { Text(stringResource(R.string.station_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null
                )
            }
        )
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
        )
    }
}

@Composable
private fun LinesOutlinedField(
    selectedLines: List<Line>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    MultiLineClickableField(
        lines = selectedLines.map { "${it.name.value} → ${it.directions.joinToString(", ")}" },
        onClick = onClick,
        enabled = enabled,
        isError = isError,
        modifier = modifier,
        label = { Text(stringResource(R.string.lines)) },
        placeholder = { Text(stringResource(R.string.select_line)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Tram,
                contentDescription = null
            )
        }
    )
}