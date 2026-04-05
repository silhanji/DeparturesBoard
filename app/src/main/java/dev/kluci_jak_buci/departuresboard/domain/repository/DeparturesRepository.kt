package dev.kluci_jak_buci.departuresboard.domain.repository

import dev.kluci_jak_buci.departuresboard.domain.model.Departure
import dev.kluci_jak_buci.departuresboard.domain.model.Profile

/**
 * Repository for fetching departures from Golemio API
 */
interface DeparturesRepository {

    /**
     * Fetches closest departures for given profile
     *
     * @param profile Profile for which departures will be fetched
     * @param limit Maximum number of departures to fetch
     */
    suspend fun get(
        profile: Profile,
        limit: Int = 5,
    ): List<Departure>
}