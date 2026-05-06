package dev.kluci_jak_buci.departuresboard.data.local

import dev.kluci_jak_buci.departuresboard.domain.model.GeoPosition
import dev.kluci_jak_buci.departuresboard.domain.model.Line
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.LineType
import dev.kluci_jak_buci.departuresboard.domain.model.Platform
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName
import dev.kluci_jak_buci.departuresboard.domain.repository.StationsRepository
import javax.inject.Inject

/**
 * Local repository with mock readonly data till stations are taken from API or database
 */
class LocalStationsRepository @Inject constructor() : StationsRepository {

    private val _stations = listOf(
        Station(
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
        ),

        Station(
            name = StationName("Malostranské náměstí"),
            platforms = listOf(
                Platform(
                    id = PlatformId("U361Z1P"),
                    name = "Malostranské náměstí - směr Újezd",
                    lines = listOf(
                        Line(
                            name = LineName("22"),
                            type = LineType.TRAM,
                            directions = listOf("Bílá Hora")
                        ),
                        Line(
                            name = LineName("23"),
                            type = LineType.TRAM,
                            directions = listOf("Královka")
                        )
                    ),
                    position = GeoPosition(50.0875, 14.4042)
                ),
                Platform(
                    id = PlatformId("U361Z2P"),
                    name = "Malostranské náměstí - směr Malostranská",
                    lines = listOf(
                        Line(
                            name = LineName("22"),
                            type = LineType.TRAM,
                            directions = listOf("Nádraží Hostivař")
                        )
                    ),
                    position = GeoPosition(50.0876, 14.4045)
                )
            )
        ),

        Station(
            name = StationName("Anděl"),
            platforms = listOf(
                Platform(
                    id = PlatformId("U362Z1P"),
                    name = "Anděl - Na Knížecí",
                    lines = listOf(
                        Line(
                            name = LineName("4"),
                            type = LineType.TRAM,
                            directions = listOf("Kubánské náměstí")
                        ),
                        Line(
                            name = LineName("9"),
                            type = LineType.TRAM,
                            directions = listOf("Spojovací")
                        ),
                        Line(
                            name = LineName("B"),
                            type = LineType.METRO,
                            directions = listOf("Černý Most")
                        )
                    ),
                    position = GeoPosition(50.0685, 14.4039)
                ),
                Platform(
                    id = PlatformId("U362Z2P"),
                    name = "Anděl - směr Zličín",
                    lines = listOf(
                        Line(
                            name = LineName("B"),
                            type = LineType.METRO,
                            directions = listOf("Zličín")
                        )
                    ),
                    position = GeoPosition(50.0686, 14.4043)
                )
            )
        ),

        Station(
            name = StationName("Budějovická"),
            platforms = listOf(
                Platform(
                    id = PlatformId("U363Z1P"),
                    name = "Budějovická - směr centrum",
                    lines = listOf(
                        Line(
                            name = LineName("C"),
                            type = LineType.METRO,
                            directions = listOf("Letňany")
                        )
                    ),
                    position = GeoPosition(50.0447, 14.4489)
                ),
                Platform(
                    id = PlatformId("U363Z2P"),
                    name = "Budějovická - směr Háje",
                    lines = listOf(
                        Line(
                            name = LineName("C"),
                            type = LineType.METRO,
                            directions = listOf("Háje")
                        )
                    ),
                    position = GeoPosition(50.0446, 14.4492)
                )
            )
        ),

        Station(
            name = StationName("Hlavní nádraží"),
            platforms = listOf(
                Platform(
                    id = PlatformId("U364Z1P"),
                    name = "Hlavní nádraží - tram",
                    lines = listOf(
                        Line(
                            name = LineName("5"),
                            type = LineType.TRAM,
                            directions = listOf("Slivenec")
                        ),
                        Line(
                            name = LineName("9"),
                            type = LineType.TRAM,
                            directions = listOf("Spojovací")
                        )
                    ),
                    position = GeoPosition(50.0830, 14.4353)
                ),
                Platform(
                    id = PlatformId("U364Z2P"),
                    name = "Hlavní nádraží - metro",
                    lines = listOf(
                        Line(
                            name = LineName("C"),
                            type = LineType.METRO,
                            directions = listOf("Letňany")
                        ),
                        Line(
                            name = LineName("C"),
                            type = LineType.METRO,
                            directions = listOf("Háje")
                        )
                    ),
                    position = GeoPosition(50.0832, 14.4348)
                )
            )
        )
    )

    override suspend fun search(needle: String): List<StationName> {
        val query = needle.trim()
        if (query.isEmpty()) return emptyList()

        return _stations
            .asSequence()
            .map { it.name }
            .filter { stationName ->
                stationName.value.contains(query, ignoreCase = true)
            }
            .distinct()
            .sortedBy { it.value }
            .toList()
    }

    override suspend fun get(name: StationName): Station? {
        return _stations.firstOrNull { station ->
            station.name.value.equals(name.value, ignoreCase = true)
        }
    }
}