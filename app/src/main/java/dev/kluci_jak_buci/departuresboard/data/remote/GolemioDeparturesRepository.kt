package dev.kluci_jak_buci.departuresboard.data.remote

import android.util.Log
import dev.kluci_jak_buci.departuresboard.domain.model.Departure
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.repository.DeparturesRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

/**
 * Maximum number of stations Golemio API allows querying in single request.
 */
private const val GOLEMIO_API_STATION_LIMIT = 50

class GolemioDeparturesRepository @Inject constructor(
    private val golemioApi: GolemioApiService
) : DeparturesRepository {

    override suspend fun get(
        profile: Profile,
        limit: Int
    ): List<Departure> {
        val departures = getFromApi(listOf(profile), limit)
        return departures.first()
    }

    override suspend fun get(
        profiles: List<Profile>,
        limit: Int
    ): Map<Profile, List<Departure>> {
        // Split profiles into groups where total number of platforms
        // is at most GOLEMIO_API_STATION_LIMIT at each group. Then call Golemio API for each
        // group and compose all the response into single Map.
        var startIndex = 0
        var currentCount = 0

        val departures: MutableList<List<Departure>> = mutableListOf()

        for(i in 0..<profiles.size) {
            val profileCount = profiles[i].selectedLines
                .map { it.platform.value }
                .distinct()
                .size

            if(currentCount + profileCount > GOLEMIO_API_STATION_LIMIT) {
                departures.addAll(
                    getFromApi(profiles.subList(startIndex, i), limit)
                )

                startIndex = i
                currentCount = 0
            }

            currentCount += profileCount
        }

        if(currentCount > 0) {
            departures.addAll(
                getFromApi(profiles, limit)
            )
        }

        return profiles
            .mapIndexed { index, profile ->
                profile to departures[index]
            }
            .toMap()
    }

    /**
     * Calls Golemio API and returns departures for given profiles. Total number of platforms has
     * to be at most 50.
     */
    private suspend fun getFromApi(
        profiles: List<Profile>,
        limit: Int
    ): List<List<Departure>> {
        return runCatching {
            val stationIds = profiles.map {
                it.selectedLines
                    .map { it.platform.value }
                    .distinct()
            }

            val totalStations = stationIds.sumOf { it.size }
            if(totalStations > GOLEMIO_API_STATION_LIMIT) {
                throw IllegalArgumentException(
                    "Golemio API limits the number of platforms to $GOLEMIO_API_STATION_LIMIT. Provided: $totalStations"
                )
            }

            val routeShortNames = profiles
                .flatMap { it.selectedLines.map { it.line.value } }
                .distinct()

            val response = golemioApi.getDepartures(
                stationIds = stationIds.toGolemioJsonQuery(),
                routeShortNames = routeShortNames,
                limit = limit,
                minutesBefore = 5,
                minutesAfter = 60,
            )

            val now = Clock.System.now()

            response
                .map { group ->
                    group.map { apiDeparture ->
                        val delay =  apiDeparture.departure.delaySeconds ?: 0

                        Departure(
                            line = LineName(apiDeparture.route.shortName),
                            scheduled = apiDeparture.departure.timestampScheduled,
                            predicted = apiDeparture.departure.timestampPredicted,
                            delay = delay.seconds,
                            headsign = apiDeparture.trip.headsign,
                        )
                    }
                }
                .map { group ->
                    group.filter { it.predicted >= now }
                }
        }.onFailure { e ->
            Log.e(null, "Error when getting departures", e)
        }.getOrThrow()
    }
}

private fun List<List<String>>.toGolemioJsonQuery(): List<String> {
    return this.mapIndexed { i, ids -> """{"$i":${Json.encodeToString(ids)}}""" }
}
