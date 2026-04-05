package dev.kluci_jak_buci.departuresboard.domain.repository

import dev.kluci_jak_buci.departuresboard.domain.model.Station
import dev.kluci_jak_buci.departuresboard.domain.model.StationName

/**
 * Repository for fetching list of all available station in PID
 */
interface StationsRepository {
    /**
     * Searches for all possible station names matching provided needle
     *
     * @param needle Station name to search for
     */
    suspend fun search(needle: String): List<StationName>

    /**
     * Gets station based on it's name
     *
     * @param name Station name, has to be exact, use search to get exact station name
     * based on user input
     */
    suspend fun get(name: StationName): Station?
}