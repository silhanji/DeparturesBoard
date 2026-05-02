package dev.kluci_jak_buci.departuresboard.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.time.Instant

interface GolemioApiService {
    @GET("v2/public/departureboards")
    suspend fun getDepartures(
        @Query("stopIds[]") stationIds: List<String>,
        @Query("limit") limit: Number = 5,
        @Query("routeShortNames") routeShortNames: List<String>? = null,
        @Query("minutesAfter") minutesAfter: Number = 60,
        @Query("minutesBefore") minutesBefore: Number = 0,
    ): List<List<DepartureGroup>>
}

data class DepartureGroup(
    val departure: Departure,
    val stop: Stop,
    val route: Route,
    val trip: Trip,
    val vehicle: Vehicle,
)

data class Departure(
    @SerializedName("timestamp_scheduled")
    val timestampScheduled: Instant,
    @SerializedName("timestamp_predicted")
    val timestampPredicted: Instant,
    @SerializedName("delay_seconds")
    val delaySeconds: Int?,
    val minutes: Int,
)

data class Stop(
    val id: String,
    val sequence: Int,
    @SerializedName("platform_code")
    val platformCode: String?,
)

data class Route(
    val type: RouteType?, // Not nullable in Goleamio API, however if the enum changes let
                          // Retrofit add null here instead of throwing exception
    @SerializedName("short_name")
    val shortName: String,
)

enum class RouteType {
    @SerializedName("tram")
    TRAM,

    @SerializedName("metro")
    METRO,

    @SerializedName("train")
    TRAIN,

    @SerializedName("bus")
    BUS,

    @SerializedName("ferry")
    FERRY,

    @SerializedName("funicular")
    FUNICULAR,

    @SerializedName("trolleybus")
    TROLLEYBUS,

    @SerializedName("ext_miscellaneous")
    OTHER,
}

data class Trip(
    val id: String,
    val headsign: String,
    @SerializedName("is_canceled")
    val isCanceled: Boolean?
)

data class Vehicle(
    val id: String?,
    @SerializedName("is_wheelchair_accessible")
    val isWheelChairAccessible: Boolean?,
    @SerializedName("is_air_conditioned")
    val isAirConditioned: Boolean?,
    @SerializedName("has_charger")
    val hasCharger: Boolean?,
)
