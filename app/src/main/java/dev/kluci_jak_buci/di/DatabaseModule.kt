package dev.kluci_jak_buci.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.kluci_jak_buci.departuresboard.data.local.db.profiles.ProfilesDatabase
import dev.kluci_jak_buci.departuresboard.data.local.db.profiles.ProfileDao
import dev.kluci_jak_buci.departuresboard.data.local.db.progressiveLevenshtein
import dev.kluci_jak_buci.departuresboard.data.local.db.stations.StationDao
import dev.kluci_jak_buci.departuresboard.data.local.db.stations.StationsDatabase
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabaseConfiguration
import io.requery.android.database.sqlite.SQLiteFunction
import javax.inject.Singleton

const val PROFILES_DB_NAME = "profiles_database"

const val STATIONS_DB_NAME = "stations_database"
const val STATIONS_DB_ASSET = "stations.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProfilesDatabase(
        @ApplicationContext context: Context
    ): ProfilesDatabase {
        return Room.databaseBuilder(
            context,
            ProfilesDatabase::class.java,
            PROFILES_DB_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true) // TODO: Remove
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(database: ProfilesDatabase): ProfileDao {
        return database.profileDao()
    }

    @Provides
    @Singleton
    fun provideStationsDatabase(
        @ApplicationContext context: Context
    ): StationsDatabase {
        return Room.databaseBuilder(
            context,
            StationsDatabase::class.java,
            STATIONS_DB_NAME
        )
            .createFromAsset(STATIONS_DB_ASSET)
            .openHelperFactory { configuration ->
                val config = SQLiteDatabaseConfiguration(
                    context.getDatabasePath(STATIONS_DB_NAME).path,
                    SQLiteDatabase.OPEN_CREATE or SQLiteDatabase.OPEN_READWRITE,
                )

                config.functions.add(
                    SQLiteFunction("levenshtein", 2) { args, result ->
                        if(args != null && result != null) {
                            val str1 = args.getString(0)
                            val str2 = args.getString(1)
                            val distance = progressiveLevenshtein(str1, str2)
                            result.set(distance)
                        }
                    }
                )

                val options = RequerySQLiteOpenHelperFactory.ConfigurationOptions { config }
                RequerySQLiteOpenHelperFactory(listOf(options)).create(configuration)
            }
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideStationDao(database: StationsDatabase): StationDao {
        return database.stationDao()
    }
}
