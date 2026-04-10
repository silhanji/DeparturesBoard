package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.domain.model.GeoPosition
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.LineType
import dev.kluci_jak_buci.departuresboard.domain.model.Platform
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.ui.components.ScreenScaffold
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.FoundStations
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchInput
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationScreen
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun SelectLinesScreen(
    selectLinesState: SelectLinesState,
    onBackArrowClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    ScreenScaffold(
        title = stringResource(R.string.select_line),
        content = { modifier ->
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = selectLinesState.lines,
                    ) { line ->
                        LineItem(
                            line = line,
                            isSelected = selectLinesState.selectedLines.contains(line),
                            onClick = { selectLinesState.selectLine(line) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = { onBackArrowClick },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.back),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Button(
                        onClick = { onConfirmClick() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        },
        onBackArrowClick = onBackArrowClick
    )
}

@Composable
fun LineItem(
    line: Line,
    isSelected: Boolean,
    onClick: () -> Unit,
    showCheckBox: Boolean = true,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
//            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showCheckBox) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = line.name.value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                for (direction in line.directions) {
                    if (direction.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = direction,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isSelected) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = stringResource(R.string.selected),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SelectLinesScreenPreview() {
    val station = Station(
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
                        name = LineName("18"),
                        type = LineType.TRAM,
                        directions = listOf("Nádraží Podbaba")
                    ),
                    Line(
                        name = LineName("A"),
                        type = LineType.METRO,
                        directions = listOf("Depo Hostivař")
                    )
                ),
                position = GeoPosition(50.0910, 14.4112)
            ),
            Platform(
                id = PlatformId("U360Z2P"),
                name = "Malostranská - směr Hradčanská",
                lines = listOf(
                    Line(
                        name = LineName("12"),
                        type = LineType.TRAM,
                        directions = listOf("Lehovec")
                    ),
                    Line(
                        name = LineName("18"),
                        type = LineType.TRAM,
                        directions = listOf("Petřiny")
                    ),
                    Line(
                        name = LineName("A"),
                        type = LineType.METRO,
                        directions = listOf("Nemocnice Motol", "Nemocnice Motol2")
                    )
                ),
                position = GeoPosition(50.0911, 14.4108)
            )
        )
    )

    val uiState = remember { mutableStateOf(SelectLinesState(station, mutableListOf())) }

    DeparturesBoardTheme {
        SelectLinesScreen(
            selectLinesState = uiState.value,
            onBackArrowClick = {},
            onConfirmClick = {},
        )
    }
}