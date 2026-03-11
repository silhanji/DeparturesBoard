package dev.silhan.departuresboard.network

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.golemio.cz";

private const val API_KEY = "YOUR_SECRET_API_KEY";

private val httpClient = OkHttpClient.Builder()
    // Add API KEY to every request
    .addInterceptor { chain ->
        val modifiedRequest = chain.request().newBuilder()
            .header("X-Access-Token", API_KEY)
            .build()
        chain.proceed(modifiedRequest)
    }
    // Convert Station ID to JSON structure
    .addInterceptor { chain ->
        val request = chain.request();

        val stationId = request.url.queryParameter("json_stopId");
        if(stationId != null) {
            val modifierUrl = request.url.newBuilder()
                .removeAllQueryParameters("json_stopId")
                .addQueryParameter("stopIds", "{\"0\": [\"$stationId\"]}")
                .build()
            val modifiedRequest = request.newBuilder()
                .url(modifierUrl)
                .build()
            chain.proceed(modifiedRequest)
        } else {
            chain.proceed(request)
        }
    }
    .build();

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .baseUrl(BASE_URL)
    .build()

interface GolemioApiService {

    @GET("v2/gtfs/stops")
    suspend fun getStops(
        @Query("names") name: String,
        @Query("offset") offset: Number = 0,
        @Query("limit") limit: Number = 10,
    ): StationsResponse

    @GET("v2/public/departureboards")
    suspend fun getDepartures(
        @Query("json_stopId") stationId: String,
        @Query("limit") limit: Number = 5,
        @Query("minutesAfter") minutesAfter: Number = 60,
        @Query("minutesBefore") minutesBefore: Number = 0,
    ): List<List<DepartureGroup>>
}

object GolemioApi {
    val retrofitService: GolemioApiService by lazy {
        retrofit.create(GolemioApiService::class.java)
    }
}

data class StationsResponse(
    val features: List<Feature>,
    val type: String,
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
)

data class Geometry(
    val coordinates: List<Double>,
    val type: String,
)

data class Properties(
    @SerializedName("location_type")
    val locationType: Number,
    @SerializedName("parent_station")
    val parentStation: String?,
    @SerializedName("platform_code")
    val platformCode: String?,
    @SerializedName("stop_id")
    val stopId: String,
    @SerializedName("stop_name")
    val stopName: String,
    @SerializedName("wheelchair_boarding")
    val wheelchairBoarding: Number,
    @SerializedName("zone_id")
    val zoneId: String,
    @SerializedName("level_id")
    val levelId: String?,
)

data class DepartureGroup(
    val departure: Departure,
    val route: Route,
)

data class Departure(
    @SerializedName("timestamp_scheduled")
    val timestampScheduled: String,
    @SerializedName("timestamp_predicted")
    val timestampPredicted: String,
    @SerializedName("delay_seconds")
    val delaySeconds: Number?,
    val minutes: Number,
)

data class Route(
    val type: String,
    @SerializedName("short_name")
    val shortName: String,
)

