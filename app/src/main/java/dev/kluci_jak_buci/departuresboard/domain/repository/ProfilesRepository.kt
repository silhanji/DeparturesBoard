package dev.kluci_jak_buci.departuresboard.domain.repository

import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import kotlinx.coroutines.flow.Flow

/**
 * Repository for locally storing and fetching user defined profiles
 */
interface ProfilesRepository {
    fun get(id: ProfileId): Flow<Profile?>
    fun getAll(): Flow<List<Profile>>
    suspend fun create(profile: Profile)
    suspend fun update(profile: Profile)
    suspend fun delete(id: ProfileId)
}