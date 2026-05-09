package dev.kluci_jak_buci.departuresboard.data.local

import dev.kluci_jak_buci.departuresboard.data.local.db.stations.StationDao
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

class LocalStationsRepository @Inject constructor(
    val dao: StationDao
) : StationsRepository {

    override suspend fun search(needle: String): List<StationName> {
        val query = needle.trim()
        if (query.isEmpty())
            return emptyList()

        return dao
            .getStationNames(
                needle = query,
                limit = 10
            )
            .map { StationName(it.name) }
            .toList()
    }

    override suspend fun get(name: StationName): Station? {
        val dbStation = dao.getStation(name.value) ?: return null

        return Station(
            name = StationName(dbStation.station.name),
            platforms = dbStation.platforms.map { dbPlatform ->
                Platform(
                    id = PlatformId(dbPlatform.platform.id),
                    name = dbPlatform.platform.name,
                    lines = dbPlatform.lines.map { dbLine ->
                        Line(
                            name = LineName(dbLine.name),
                            type = fromDbLineType(dbLine.type),
                            directions = if(dbLine.direction2 != null)
                                listOf(dbLine.direction, dbLine.direction2)
                            else
                                listOf(dbLine.direction)
                        )
                    },
                    position = GeoPosition(
                        latitude = dbPlatform.platform.latitude,
                        longitude = dbPlatform.platform.longitude
                    )
                )
            }
        )
    }

    fun fromDbLineType(dbType: String): LineType {
        return when(dbType.lowercase()) {
            "tram" -> LineType.TRAM
            "bus" -> LineType.BUS
            "metro" -> LineType.METRO
            "ferry" -> LineType.FERRY
            "trolleybus" -> LineType.TROLLEYBUS
            else -> throw IllegalArgumentException("Unknown line type: $dbType")
        }
    }
}