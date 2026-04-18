@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.ui.components.ScreenScaffold
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.FoundStationItem
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
        content = { modifier ->
            Box(modifier = modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        ProfileHeader()
                    }

                    item {
                        SectionWrapper(
                            title = stringResource(R.string.profile_details),
                            icon = Icons.Default.Edit,
                        ) {
                            OutlinedTextField(
                                value = state.name.value,
                                onValueChange = onNameChange,
                                label = { Text(text = stringResource(R.string.name)) },
                                isError = state.name.isError,
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    state.name.errorMessage?.let { Text(text = it) }
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    item {
                        SectionWrapper(
                            title = stringResource(R.string.time_filter),
                            icon = Icons.Default.AccessTime,
                        ) {
                            TimeFilterSection(
                                allDay = state.allDay,
                                timeFilter = state.timeFilter.value,
                                onAllDayChange = onAllDayChange,
                                onTimeFilterChange = onTimeFilterChange
                            )
                        }
                    }

                    item {
                        val hasStation = state.selectedStation != null
                        SectionWrapper(
                            title = if (hasStation) stringResource(R.string.departures) else stringResource(R.string.select_station),
                            icon = Icons.Default.Business,
                            onActionClick = { showStationBottomSheet = true },
                            actionLabel = if (!hasStation) stringResource(R.string.add) else stringResource(R.string.edit)
                        ) {
                            Column {
                                if (hasStation) {
                                    FoundStationItem(
                                        station = state.selectedStation!!,
                                        onClick = {},
                                        modifier = Modifier.padding(horizontal = 0.dp)
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Lines Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Route,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(R.string.lines),
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        val hasLines = state.selectedLines.value.isNotEmpty()
                                        TextButton(
                                            onClick = { showLinesBottomSheet = true },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (!hasLines) Icons.Default.Add else Icons.Default.Edit,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = if (!hasLines) stringResource(R.string.add) else stringResource(R.string.edit),
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Concise Lines Display
                                    if (state.selectedLines.value.isEmpty()) {
                                        Text(
                                            text = stringResource(R.string.no_lines_selected_hint),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    } else {
                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            state.selectedLines.value.forEach { selectedLine ->
                                                val lineInfo = state.resolvedLines.find { it.name == selectedLine.line }
                                                SelectedLineChip(
                                                    lineName = selectedLine.line.value,
                                                    directions = lineInfo?.directions ?: emptyList()
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Station Placeholder Hint
                                    Text(
                                        text = "Select a station to configure departures and lines.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (showStationBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showStationBottomSheet = false },
                        sheetState = searchStationSheetState,
//                        dragHandle = null
                    ) {
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
//                        dragHandle = null
                    ) {
                        SelectLines(
                            lines = state.resolvedLines,
                            selectedLines = state.resolvedLines.filter { line ->
                                state.selectedLines.value.any { it.line == line.name }
                            },
                            onLineClick = onLineClick,
                            modifier = Modifier.padding(top = 24.dp)
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
fun BottomSheet(

) {

}

@Composable
private fun SelectedLineChip(lineName: String, directions: List<String>) {
    AssistChip(
        onClick = { },
        label = {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(lineName)
                    }
                    if (directions.isNotEmpty()) {
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )) {
                            append(" → ${directions.joinToString(", ")}")
                        }
                    }
                }
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = null,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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

@Composable
private fun SectionWrapper(
    title: String,
    icon: ImageVector,
    onActionClick: (() -> Unit)? = null,
    actionLabel: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.weight(1f))

            if (onActionClick != null) {
                TextButton(
                    onClick = onActionClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (actionLabel == stringResource(R.string.add)) Icons.Default.Add else Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = actionLabel ?: stringResource(R.string.edit),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        content()
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun TimeFilterSection(
    allDay: Boolean,
    timeFilter: TimeFilter,
    onAllDayChange: () -> Unit,
    onTimeFilterChange: (TimeFilter) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAllDayChange() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.all_day),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Show departures at any time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = allDay,
                    onCheckedChange = { onAllDayChange() }
                )
            }

            AnimatedVisibility(
                visible = !allDay,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
        // Invisible overlay to intercept clicks
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
private fun OutlinedButtonWithIcon(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
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
