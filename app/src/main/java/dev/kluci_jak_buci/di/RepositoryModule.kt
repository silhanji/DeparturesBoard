package dev.kluci_jak_buci.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kluci_jak_buci.departuresboard.data.local.LocalProfilesRepository
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfilesRepository(
        localProfilesRepository: LocalProfilesRepository
    ): ProfilesRepository

}