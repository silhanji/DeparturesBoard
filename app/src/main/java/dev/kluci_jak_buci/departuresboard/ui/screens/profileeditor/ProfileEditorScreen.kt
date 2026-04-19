@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tram
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.ui.components.BottomSheetHeader
import dev.kluci_jak_buci.departuresboard.ui.components.ScreenScaffold
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationStandalone
import dev.kluci_jak_buci.departuresboard.ui.screens.selectlines.SelectLines
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

@Composable
fun ProfileEditorScreen(
    state: ProfileEditorState,
    onNameChange: (String) -> Unit,
    onTimeFilterChange: (TimeFilter) -> Unit,
    onAllDayChange: () -> Unit,
    onBackArrowClick: () -> Unit,
    onSelectStationClick: (StationName) -> Unit,
    onSaveClick: () -> Unit = {},
    onLineClick: (Line) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectLinesSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showLinesBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val searchStationSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showStationBottomSheet by remember { mutableStateOf(false) }


    ScreenScaffold(
        title = stringResource(R.string.create_profile),
        onBackArrowClick = onBackArrowClick,
        modifier = modifier,
        content = { modifier ->
            Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        DetailsCard(
                            name = state.name.value,
                            onNameChange = onNameChange,
                            isError = state.name.isError,
                            errorMessage = state.name.errorMessage
                        )
                    }

                    item {
                        LinesCard(
                            stationName = state.selectedStation,
                            onStationClick = { showStationBottomSheet = true },
                            selectedLines = state.resolvedLines.filter { line ->
                                state.selectedLines.value.any { it.line == line.name }
                            },
                            onLinesClick = { showLinesBottomSheet = true }
                        )
                    }

                    item {
                        TimeFilterCard(
                            allDay = state.allDay,
                            timeFilter = state.timeFilter.value,
                            onAllDayChange = onAllDayChange,
                            onTimeFilterChange = onTimeFilterChange
                        )
                    }
                }

                if (showStationBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showStationBottomSheet = false },
                        sheetState = searchStationSheetState,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        BottomSheetHeader(
                            title = stringResource(R.string.choose_station),
                            modifier = Modifier.fillMaxWidth(),
                            onBackClick = {
                                scope.launch { searchStationSheetState.hide() }.invokeOnCompletion {
                                    if (!searchStationSheetState.isVisible) {
                                        showStationBottomSheet = false
                                    }
                                }
                            }
                        )
                        SearchStationStandalone(
                            onStationSelected = { stationName ->
                                scope.launch { searchStationSheetState.hide() }.invokeOnCompletion {
                                    if (!searchStationSheetState.isVisible) {
                                        showStationBottomSheet = false
                                        onSelectStationClick(stationName)
                                    }
                                }
                            }
                        )
                    }
                }

                if (showLinesBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showLinesBottomSheet = false },
                        sheetState = selectLinesSheetState,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        BottomSheetHeader(
                            title = stringResource(R.string.select_line),
                            modifier = Modifier.fillMaxWidth(),
                            onBackClick = {
                                scope.launch { selectLinesSheetState.hide() }.invokeOnCompletion {
                                    if (!selectLinesSheetState.isVisible) {
                                        showLinesBottomSheet = false
                                    }
                                }
                            },
                            onConfirmClick = {
                                scope.launch { selectLinesSheetState.hide() }.invokeOnCompletion {
                                    if (!selectLinesSheetState.isVisible) {
                                        showLinesBottomSheet = false
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

                // Sticky Save Button
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = onSaveClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = state.isFormValid && !state.isSaving
                    ) {
                        Text(
                            text = stringResource(R.string.save_profile),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DetailsCard(
    name: String,
    onNameChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.profile_details),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = stringResource(R.string.name)) },
                isError = isError,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    errorMessage?.let { Text(text = it) }
                },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun LinesCard(
    stationName: StationName?,
    onStationClick: () -> Unit,
    selectedLines: List<Line>,
    onLinesClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.lines),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            StationOutlinedField(
                stationName = stationName,
                onClick = onStationClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinesOutlinedField(
                selectedLines = selectedLines,
                onClick = onLinesClick,
                enabled = stationName != null
            )
        }
    }
}

@Composable
fun StationOutlinedField(
    stationName: StationName?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textValue = stationName?.value ?: ""

    Box(modifier = modifier) {
        OutlinedTextField(
            value = textValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.select_station)) },
            placeholder = { Text(stringResource(R.string.station_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinesOutlinedField(
    selectedLines: List<Line>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textValue = selectedLines.joinToString(" • ") { it.name.value }

    Box(modifier = modifier) {
        BasicTextField(
            value = textValue,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = textValue,
                    innerTextField = {
                        if (selectedLines.isEmpty()) {
                            innerTextField()
                        } else {
                            Column(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                selectedLines.take(3).forEach { line ->
                                    Text(
                                        text = "${line.name.value} → ${line.directions.joinToString(", ")}",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                if (selectedLines.size > 3) {
                                    Text(
                                        text = "+${selectedLines.size - 3} more",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    enabled = enabled,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = { Text(stringResource(R.string.lines)) },
                    placeholder = { Text(stringResource(R.string.select_line)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Tram,
                            contentDescription = null
                        )
                    },
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                )
            }
        )
        // Clickable overlay to ensure click is always registered
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = enabled) { onClick() }
        )
    }
}

@Composable
fun TimeFilterCard(
    allDay: Boolean,
    timeFilter: TimeFilter,
    onAllDayChange: () -> Unit,
    onTimeFilterChange: (TimeFilter) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.time_filter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.all_day),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = allDay,
                        onCheckedChange = { onAllDayChange() }
                    )
                }
            }

            AnimatedVisibility(
                visible = !allDay,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeInputField(
                            label = stringResource(R.string.from),
                            value = timeFilter.from,
                            onValueChange = {
                                onTimeFilterChange(timeFilter.copy(from = it))
                            },
                            modifier = Modifier.weight(1f)
                        )

                        TimeInputField(
                            label = stringResource(R.string.to),
                            value = timeFilter.to,
                            onValueChange = {
                                onTimeFilterChange(timeFilter.copy(to = it))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeInputField(
    label: String,
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value.formatHourMinute(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        )
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable { showTimePicker = true }
        )
    }

    if (showTimePicker) {
        TimePickerWithDialog(
            initialTime = value,
            onConfirm = { hour, minute ->
                onValueChange(LocalTime(hour, minute))
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}

@Composable
fun TimePickerWithDialog(
    initialTime: LocalTime,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(state = timePickerState)
            }
        }
    )
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Customize your view",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Configure which departures you want to see and when they should be active.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun LocalTime.formatHourMinute(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileEditorScreenPreview() {
    var state by remember { mutableStateOf(ProfileEditorState()) }

    DeparturesBoardTheme {
        ProfileEditorScreen(
            state = state,
            onNameChange = { state = state.copy(name = state.name.copy(value = it)) },
            onAllDayChange = { state = state.copy(allDay = !state.allDay) },
            onTimeFilterChange = { state = state.copy(timeFilter = state.timeFilter.copy(value = it)) },
            onSelectStationClick = {},
            onBackArrowClick = {}
        )
    }
}
