@file:OptIn(ExperimentalMaterial3Api::class)
package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import android.widget.NumberPicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.LineType
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.ui.components.ScreenScaffold
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.FoundStationItem
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationScreen
import dev.kluci_jak_buci.departuresboard.ui.screens.selectlines.LineItem
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun ProfileEditorScreen(
    state: ProfileEditorState,
    onNameChange: (String) -> Unit,
    onTimeFilterChange: (TimeFilter) -> Unit,
    onAllDayChange: () -> Unit,
    onBackArrowClick: () -> Unit,
) {

    ScreenScaffold(
        title = "Create Profile",
        content = { modifier ->
            Column(modifier = modifier) {
                OutlinedTextField(
                    value = state.name.value,
                    onValueChange = onNameChange,
                    label = { Text(text = stringResource(R.string.name)) },
                    isError = state.name.isError,
                    supportingText = {
                        state.name.errorMessage?.let {
                            Text(text = it)
                        }
                    }
                )

                Row() {
                    Text(
                        text = "Time Filter"
                    )

                    Text(
                        text = "all day"
                    )
                    Switch(
                        checked = state.allDay,
                        onCheckedChange =  { onAllDayChange() }
                    )
                }

                if (!state.allDay) {
                    TimeInputField(
                        label = "From",
                        value = state.timeFilter.value?.from ?: LocalTime(0, 0),
                        onValueChange = {
                            onTimeFilterChange(TimeFilter(
                                it,
                                state.timeFilter.value?.to ?: LocalTime(23, 45)
                            ))
                        }
                    )

                    TimeInputField(
                        label = "To",
                        value = state.timeFilter.value?.to ?: LocalTime(0, 0),
                        onValueChange = {
                            onTimeFilterChange(TimeFilter(
                                state.timeFilter.value?.from ?: LocalTime(23, 45),
                                it
                            ))
                        }
                    )
                }

                FoundStationItem(
                    station = StationName("Andel"),
                    onClick = {}
                )

                LineItem(
                    line = Line(
                        name = LineName("12"),
                        type = LineType.TRAM,
                        directions = listOf("Lehovec")
                    ),
                    isSelected = false,
                    onClick = {},
                    showCheckBox = false
                )

//                TimeFilterPickers(
//                    timeFilter = state.timeFilter.value,
//                    onTimeFilterChange = onTimeFilterChange
//                )
//
//                TimeFilterRangeSlider(
//                    timeFilter = state.timeFilter.value,
//                    onTimeFilterChange = onTimeFilterChange
//                )
//
//                TimeFilterWheelPickers(
//                    timeFilter = state.timeFilter.value,
//                    onTimeFilterChange = onTimeFilterChange
//                )

            }


        },
        onBackArrowClick = onBackArrowClick
    )


}

@Composable
fun TimeInputField(
    label: String,
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value.formatHourMinute(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Time") },
            placeholder = { Text("Select time") },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable { showTimePicker = true }
        )
    }

    if (showTimePicker) {
        DialWithDialogExample(
            onConfirm = { timePickerState ->
                onValueChange(LocalTime(
                    timePickerState.hour,
                    timePickerState.minute
                ))
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}

@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = LocalTime(8, 0)

    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 10,
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}



data class TimeFilter(
    val from: LocalTime,
    val to: LocalTime,
)

@Composable
fun TimeFilterWheelPickers(
    timeFilter: TimeFilter?,
    onTimeFilterChange: (TimeFilter?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showFromDialog by remember { mutableStateOf(false) }
    var showToDialog by remember { mutableStateOf(false) }

    val currentFrom = timeFilter?.from ?: LocalTime(0, 0)
    val currentTo = timeFilter?.to ?: LocalTime(23, 45)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TimePickerField(
            value = currentFrom.formatHourMinute(),
            label = "From",
            modifier = Modifier.weight(1f),
            onClick = { showFromDialog = true }
        )

        TimePickerField(
            value = currentTo.formatHourMinute(),
            label = "To",
            modifier = Modifier.weight(1f),
            onClick = { showToDialog = true }
        )
    }

    if (showFromDialog) {
        WheelTimePickerDialog(
            initialTime = currentFrom,
            title = "Select start time",
            onDismiss = { showFromDialog = false },
            onConfirm = { newFrom ->
                showFromDialog = false
                onTimeFilterChange(
                    TimeFilter(
                        from = newFrom,
                        to = if (newFrom > currentTo) newFrom else currentTo
                    )
                )
            }
        )
    }

    if (showToDialog) {
        WheelTimePickerDialog(
            initialTime = currentTo,
            title = "Select end time",
            onDismiss = { showToDialog = false },
            onConfirm = { newTo ->
                showToDialog = false
                onTimeFilterChange(
                    TimeFilter(
                        from = if (newTo < currentFrom) newTo else currentFrom,
                        to = newTo
                    )
                )
            }
        )
    }
}

@Composable
private fun TimePickerField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier.clickable { onClick() },
        readOnly = true,
        enabled = false,
        label = { Text(label) }
    )
}

@Composable
fun WheelTimePickerDialog(
    initialTime: LocalTime,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
) {
    val minuteValues = listOf(0, 15, 30, 45)

    var selectedHour by remember { mutableIntStateOf(initialTime.hour) }
    var selectedMinuteIndex by remember {
        mutableIntStateOf(
            minuteValues.indexOf(initialTime.minute).takeIf { it >= 0 } ?: 0
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NumberPickerView(
                    value = selectedHour,
                    range = 0..23,
                    format = { it.toString().padStart(2, '0') },
                    onValueChange = { selectedHour = it },
                    modifier = Modifier.weight(1f)
                )

                NumberPickerView(
                    value = selectedMinuteIndex,
                    range = minuteValues.indices,
                    format = { minuteValues[it].toString().padStart(2, '0') },
                    onValueChange = { selectedMinuteIndex = it },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(LocalTime(selectedHour, minuteValues[selectedMinuteIndex]))
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun NumberPickerView(
    value: Int,
    range: IntRange,
    format: (Int) -> String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                wrapSelectorWheel = true
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

                displayedValues = Array(range.count()) { index ->
                    format(range.first + index)
                }

                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = { picker ->
            if (picker.minValue != range.first || picker.maxValue != range.last) {
                picker.displayedValues = null
                picker.minValue = range.first
                picker.maxValue = range.last
                picker.displayedValues = Array(range.count()) { index ->
                    format(range.first + index)
                }
            }

            if (picker.value != value) {
                picker.value = value
            }
        }
    )
}

private fun LocalTime.formatHourMinute(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}

@Composable
fun TimeFilterPickers(
    timeFilter: TimeFilter?,
    onTimeFilterChange: (TimeFilter?) -> Unit,
    modifier: Modifier = Modifier,
    labelFrom: String = "From",
    labelTo: String = "To",
) {
    val context = LocalContext.current

    val currentFrom = timeFilter?.from ?: LocalTime(0, 0)
    val currentTo = timeFilter?.to ?: LocalTime(23, 45)

    val fromPicker = remember(currentFrom, currentTo) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newFrom = LocalTime(hour, minute)
                val adjustedTo = if (newFrom > currentTo) newFrom else currentTo
                onTimeFilterChange(
                    TimeFilter(
                        from = newFrom,
                        to = adjustedTo
                    )
                )
            },
            currentFrom.hour,
            currentFrom.minute,
            true
        )
    }

    val toPicker = remember(currentFrom, currentTo) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newTo = LocalTime(hour, minute)
                val adjustedFrom = if (newTo < currentFrom) newTo else currentFrom
                onTimeFilterChange(
                    TimeFilter(
                        from = adjustedFrom,
                        to = newTo
                    )
                )
            },
            currentTo.hour,
            currentTo.minute,
            true
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TimePickerField2(
            value = currentFrom.formatAsHourMinute(),
            label = labelFrom,
            onClick = { fromPicker.show() },
            modifier = Modifier.weight(1f)
        )

        TimePickerField2(
            value = currentTo.formatAsHourMinute(),
            label = labelTo,
            onClick = { toPicker.show() },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimePickerField2(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier.clickable(onClick = onClick),
        readOnly = true,
        enabled = false,
        label = { Text(label) }
    )
}

private fun LocalTime.formatAsHourMinute(): String {
    val hh = hour.toString().padStart(2, '0')
    val mm = minute.toString().padStart(2, '0')
    return "$hh:$mm"
}

@Composable
fun TimeFilterRangeSlider(
    timeFilter: TimeFilter?,
    onTimeFilterChange: (TimeFilter?) -> Unit,
    modifier: Modifier = Modifier
) {
    val startMinutes = timeFilter?.from?.toMinutes()?.toFloat() ?: (8 * 60f)
    val endMinutes = timeFilter?.to?.toMinutes()?.toFloat() ?: (16 * 60f)

    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Validity",
                style = MaterialTheme.typography.labelLarge
            )

//            Text(
//                text = "${minutesToLocalTime(startMinutes.roundToInt()).format(formatter)} - " +
//                        "${minutesToLocalTime(endMinutes.roundToInt()).format(formatter)}"
//            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = startMinutes..endMinutes,
            onValueChange = { range ->
                val snappedFrom = snapToQuarterHour(range.start)
                val snappedTo = snapToQuarterHour(range.endInclusive)

                if (snappedFrom < snappedTo) {
                    onTimeFilterChange(TimeFilter(minutesToLocalTime(snappedFrom), minutesToLocalTime(snappedTo)))
                }
            },
            valueRange = 0f..1439f
        )

    }
}

private fun snapToQuarterHour(value: Float): Int {
    val minutes = value.roundToInt()
    val snapped = (minutes / 15.0).roundToInt() * 15
    return snapped.coerceIn(0, 23 * 60 + 45)
}

private fun minutesToLocalTime(totalMinutes: Int): LocalTime =
    LocalTime(totalMinutes / 60, totalMinutes % 60)

private fun LocalTime.toMinutes(): Int =
    hour * 60 + minute


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ProfileEditorScreenPreview() {
    var state by remember { mutableStateOf(ProfileEditorState()) }

    DeparturesBoardTheme {
        ProfileEditorScreen(
            onBackArrowClick = {},
            state = state,
            onNameChange = { state = state.copy ( name = state.name.copy(value = it)) },
            onAllDayChange = {state = state.copy (allDay = !state.allDay)},
            onTimeFilterChange = { state = state.copy(timeFilter = state.timeFilter.copy(value = it)) },
        )
    }
}