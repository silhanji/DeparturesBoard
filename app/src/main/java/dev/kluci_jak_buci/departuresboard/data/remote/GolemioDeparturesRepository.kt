package dev.kluci_jak_buci.departuresboard.data.remote

import android.util.Log
import dev.kluci_jak_buci.departuresboard.domain.model.Departure
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.repository.DeparturesRepository
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class GolemioDeparturesRepository @Inject constructor(
    private val golemioApi: GolemioApiService
) : DeparturesRepository {

    override suspend fun get(
        profile: Profile,
        limit: Int
    ): List<Departure> {
        return runCatching {
            val response = golemioApi.getDepartures(
                stationIds = profile.selectedLines.map { it.platform.value }.distinct(),
                routeShortNames = profile.selectedLines.map { it.line.value }.distinct(),
                limit = limit,
                minutesBefore = 5,
                minutesAfter = 60,
            )

            val now = Clock.System.now()

            val departures = response.flatMap { innerList ->
                innerList.map { apiDeparture ->
                    val delay =  apiDeparture.departure.delaySeconds ?: 0

                    Departure(
                        line = LineName(apiDeparture.route.shortName),
                        scheduled = apiDeparture.departure.timestampScheduled,
                        predicted = apiDeparture.departure.timestampPredicted,
                        delay = delay.seconds,
                    )
                }
            }

            departures.filter { it.predicted >= now }
        }.onFailure { e ->
            Log.e(null, "Error when getting departures", e)
        }.getOrThrow()
    }
}
