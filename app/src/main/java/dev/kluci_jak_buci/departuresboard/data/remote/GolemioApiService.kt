package dev.kluci_jak_buci.departuresboard.data.remote

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.time.Instant
import dev.kluci_jak_buci.departuresboard.BuildConfig.GOLEMIO_API_KEY

private const val BASE_URL = "https://api.golemio.cz"

val instantDeserializer= JsonDeserializer { json, _, _ ->
    json?.asString?.let { Instant.parse(it) }
}

private val httpClient = OkHttpClient.Builder()
    // Add API KEY to every request
    .addInterceptor { chain ->
        val modifiedRequest = chain.request().newBuilder()
            .header("X-Access-Token", GOLEMIO_API_KEY)
            .build()
        chain.proceed(modifiedRequest)
    }
    // Convert Station ID to JSON structure
    .addInterceptor { chain ->
        val request = chain.request()

        val stopIds = request.url.queryParameterValues("json_stopId")
        if(stopIds.isNotEmpty()) {
            val stopIdsJson = """{"0": ${Json.encodeToString(stopIds)}}"""

            val modifierUrl = request.url.newBuilder()
                .removeAllQueryParameters("json_stopId")
                .addQueryParameter("stopIds", stopIdsJson)
                .build()
            val modifiedRequest = request.newBuilder()
                .url(modifierUrl)
                .build()
            chain.proceed(modifiedRequest)
        } else {
            chain.proceed(request)
        }
    }
    .build()

private val gson = GsonBuilder()
    .registerTypeAdapter(Instant::class.java, instantDeserializer)
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(httpClient)
    .baseUrl(BASE_URL)
    .build()

interface GolemioApiService {
    @GET("v2/public/departureboards")
    suspend fun getDepartures(
        @Query("json_stopId") stationIds: List<String>,
        @Query("limit") limit: Number = 5,
        @Query("routeShortNames") routeShortNames: List<String>? = null,
        @Query("minutesAfter") minutesAfter: Number = 60,
        @Query("minutesBefore") minutesBefore: Number = 0,
    ): List<List<DepartureGroup>>
}

object GolemioApi {
    val retrofitService: GolemioApiService by lazy {
        retrofit.create(GolemioApiService::class.java)
    }
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
