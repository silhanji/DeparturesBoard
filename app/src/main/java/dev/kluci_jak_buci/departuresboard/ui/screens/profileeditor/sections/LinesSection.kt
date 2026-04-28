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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.GeoPosition
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.LineType
import dev.kluci_jak_buci.departuresboard.domain.model.Platform
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.BottomSheetHeader
import dev.kluci_jak_buci.departuresboard.ui.components.MultiLineClickableField
import dev.kluci_jak_buci.departuresboard.ui.components.Field
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.ProfileEditorState
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.searchstation.SearchStationStandalone
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.InputField
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.SelectLines
import kotlinx.coroutines.launch


/**
 * Maps the selected lines to their corresponding Line object for if the station has the line.
 */
private fun Station.getSelectedLines(selectedLines: List<SelectedLine>): List<Line> {
    return this.platforms
        .flatMap { platform -> platform.lines.map { line -> Pair( platform.id, line)} }
        .filter{ (platformId, line) ->
            selectedLines.any { it.platform == platformId && it.line == line.name }
        }
        .map { (_, line) -> line }
}

@Composable
fun LinesSection(
    state: ProfileEditorState,
    onStationClick: (StationName) -> Unit,
    onLineClick: (Line) -> Unit = {},
) {
    var showLinesBottomSheet by remember { mutableStateOf(false) }
    var showStationBottomSheet by remember { mutableStateOf(false) }

    Section(
        name = stringResource(R.string.lines)
    ) {
        StationOutlinedField(
            stationName = state.selectedStation?.name,
            onClick = { showStationBottomSheet = true }
        )
        val selectedLines = state.selectedStation
            ?.getSelectedLines(state.selectedLines.value) ?: emptyList()
        LinesOutlinedField(
            selectedLines = selectedLines,
            onClick = { showLinesBottomSheet = true },
            enabled = state.selectedStation != null
        )
    }

    SearchStationBottomSheet(
        onSelectStationClick = onStationClick,
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
                modifier = Modifier.fillMaxWidth(),
                onBackClick = {
                    scope.launch { searchStationSheetState.hide() }.invokeOnCompletion {
                        if (!searchStationSheetState.isVisible) {
                            showStationBottomSheetChange(false)
                        }
                    }
                }
            )
            SearchStationStandalone(
                onStationClick = { stationName ->
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
                modifier = Modifier.fillMaxWidth(),
                onConfirmClick = {
                    scope.launch { selectLinesSheetState.hide() }.invokeOnCompletion {
                        if (!selectLinesSheetState.isVisible) {
                            onShowLinesBottomSheetChange(false)
                        }
                    }
                }
            )
            val stationLines = state.selectedStation?.getLines() ?: emptyList()
            val selectedLines = state.selectedStation
                ?.getSelectedLines(state.selectedLines.value) ?: emptyList()

            SelectLines(
                lines = stationLines,
                selectedLines = selectedLines,
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
        lines = selectedLines.map { "${it.name.value} → ${it.directions.joinToString(", ")}" }.sortedBy { it },
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


@Preview
@Composable
fun LinesSectionPreview() {
    var state by remember {
        mutableStateOf(ProfileEditorState(
            selectedLines = InputField(listOf(
                SelectedLine(LineName("12"), PlatformId("U360Z1P")),
                SelectedLine(LineName("A"), PlatformId("U360Z1P"))
            )),
            selectedStation = Station(
                name = StationName("Malostranská"),
                platforms = listOf(
                    Platform(
                        id = PlatformId("U360Z1P"),
                        name = "Malostranská - směr centrum",
                        lines = listOf(
                            Line(
                                name = LineName("12"),
                                type = LineType.TRAM,
                                directions = listOf("Sídliště Barrandov")
                            ),
                            Line(
                                name = LineName("A"),
                                type = LineType.METRO,
                                directions = listOf("Depo Hostivař")
                            )
                        ),
                        position = GeoPosition(50.0910, 14.4112)
                    ),
                )
            )
        ))
    }

    LinesSection(
        state = state,
        onStationClick = {},
        onLineClick = {}
    )
}