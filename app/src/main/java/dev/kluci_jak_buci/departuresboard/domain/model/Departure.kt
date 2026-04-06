package dev.kluci_jak_buci.departuresboard.domain.model

import kotlin.time.Duration
import kotlin.time.Instant

/**
 * @param line Line which will depart
 * @param scheduled Time at which the vehicle is scheduled to depart the station
 * @param predicted Time at which it is predicted the vehicle will depart, including delay
 * @param delay Delay of the vehicle against it's scheduled departure time
 */
data class Departure(
    val line: LineName,
    val scheduled: Instant,
    val predicted: Instant,
    val delay: Duration,
)