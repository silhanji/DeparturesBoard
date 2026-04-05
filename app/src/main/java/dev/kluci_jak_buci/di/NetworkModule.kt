package dev.kluci_jak_buci.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kluci_jak_buci.departuresboard.BuildConfig.GOLEMIO_API_KEY
import dev.kluci_jak_buci.departuresboard.data.remote.GolemioApiService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.time.Instant

private const val GOLEMIO_BASE_URL = "https://api.golemio.cz"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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
    }

    @Provides
    @Singleton
    fun provideGolemioApiService(httpClient: OkHttpClient): GolemioApiService {
        val instantDeserializer= JsonDeserializer { json, _, _ ->
            json?.asString?.let { Instant.parse(it) }
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, instantDeserializer)
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .baseUrl(GOLEMIO_BASE_URL)
            .build()
            .create(GolemioApiService::class.java)
    }
}