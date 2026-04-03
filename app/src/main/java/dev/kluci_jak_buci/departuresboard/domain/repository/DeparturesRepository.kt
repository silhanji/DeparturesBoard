package dev.kluci_jak_buci.departuresboard.domain.repository

import dev.kluci_jak_buci.departuresboard.domain.model.Departure
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId

/**
 * Repository for fetching departures from Golemio API
 */
interface DeparturesRepository {

    /**
     * Fetches departures for given stops
     *
     * @param platforms IDs of platforms for which departures will be fetched
     * @param lines Optional list of line names, if not provided departures of all lines will
     * be fetched
     * @param limit Maximum number of departures to fetch
     */
    suspend fun get(
        platforms: List<PlatformId>,
        lines: List<LineName>? = null,
        limit: Int = 5,
    ): List<Departure>
}