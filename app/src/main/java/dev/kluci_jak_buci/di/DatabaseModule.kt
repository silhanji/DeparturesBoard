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
import dev.kluci_jak_buci.departuresboard.data.local.db.stations.StationDao
import dev.kluci_jak_buci.departuresboard.data.local.db.stations.StationsDatabase
import javax.inject.Singleton

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
            "profiles_database"
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
            "stations_database"
        )
            .createFromAsset("stations.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideStationDao(database: StationsDatabase): StationDao {
        return database.stationDao()
    }
}
