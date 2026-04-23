@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.ui.components.ScreenScaffold
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections.DetailsSection
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections.LinesSection
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections.TimeFilterSection
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun ProfileEditorScreen(
    state: ProfileEditorState,
    onNameChange: (String) -> Unit,
    onTimeFilterChange: (TimeFilter) -> Unit,
    onAllDayChange: () -> Unit,
    onBackArrowClick: () -> Unit,
    onSaveClick: () -> Unit = {},
    onSelectStationClick: (StationName) -> Unit,
    onLineClick: (Line) -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                        DetailsSection(
                            name = state.name.value,
                            onNameChange = onNameChange,
                            isError = state.name.isError,
                            errorMessage = state.name.errorMessage
                        )
                    }

                    item {
                        LinesSection(
                            state = state,
                            onSelectStationClick = onSelectStationClick,
                            onLineClick = onLineClick
                        )
                    }

                    item {
                        TimeFilterSection(
                            allDay = state.allDay,
                            timeFilter = state.timeFilter.value,
                            onAllDayChange = onAllDayChange,
                            onTimeFilterChange = onTimeFilterChange
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