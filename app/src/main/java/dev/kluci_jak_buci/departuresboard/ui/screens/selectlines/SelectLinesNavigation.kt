package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kluci_jak_buci.departuresboard.domain.model.GeoPosition
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.LineType
import dev.kluci_jak_buci.departuresboard.domain.model.Platform
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationScreen
import dev.kluci_jak_buci.departuresboard.ui.screens.searchstation.SearchStationViewModel
import kotlinx.serialization.Serializable

@Serializable
object SelectLines {
}

fun NavGraphBuilder.selectLines(
    onBackArrowClick: () -> Unit,
) {
    composable<SelectLines> {

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
                            directions = listOf("Nemocnice Motol")
                        )
                    ),
                    position = GeoPosition(50.0911, 14.4108)
                )
            )
        )

        val uiState = rememberSaveable { SelectLinesState(station, mutableListOf()) }

        SelectLinesScreen(
            selectLinesState = uiState,
            onBackArrowClick = onBackArrowClick,
            onConfirmClick = {}
        )
    }
}