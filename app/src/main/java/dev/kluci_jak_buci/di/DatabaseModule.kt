package dev.kluci_jak_buci.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.kluci_jak_buci.departuresboard.data.local.db.DeparturesBoardDatabase
import dev.kluci_jak_buci.departuresboard.data.local.db.ProfileDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDeparturesBoardDatabase(
        @ApplicationContext context: Context
    ): DeparturesBoardDatabase {
        return Room.databaseBuilder(
            context,
            DeparturesBoardDatabase::class.java,
            "departures_board_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true) // TODO: Remove
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(database: DeparturesBoardDatabase): ProfileDao {
        return database.profileDao()
    }
}
