package dev.kluci_jak_buci.departuresboard.data.local

import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LocalProfilesRepository @Inject constructor() : ProfilesRepository {

    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())

//    init {
//        // Some predefined profiles until profile creation is implemented
//        _profiles.update {
//            listOf(
//                Profile(
//                    id = ProfileId.generate(),
//                    name = "Matfyz",
//                    selectedLines = listOf(
//                        SelectedLine(
//                            line = LineName("22"),
//                            platform = PlatformId("U361Z1P")
//                        ),
//                        SelectedLine(
//                            line = LineName("15"),
//                            platform = PlatformId("U361Z1P")
//                        ),
//                    ),
//                    timeFilter = null,
//                    vehicleFilter = null,
//                ),
//                Profile(
//                    id = ProfileId.generate(),
//                    name = "Budějovická (práce)",
//                    selectedLines = listOf(
//                        SelectedLine(
//                            line = LineName("124"),
//                            platform = PlatformId("U50Z6P")
//                        ),
//                        SelectedLine(
//                            line = LineName("134"),
//                            platform = PlatformId("U50Z6P")
//                        ),
//                    ),
//                    timeFilter = null,
//                    vehicleFilter = null,
//                ),
//                Profile(
//                    id = ProfileId.generate(),
//                    name = "Vyšehrad",
//                    selectedLines = listOf(
//                        SelectedLine(
//                            line = LineName("C"),
//                            platform = PlatformId("U527Z101P")
//                        ),
//                    ),
//                    timeFilter = null,
//                    vehicleFilter = null,
//                )
//            )
//        }
//    }

    override fun get(id: ProfileId): Flow<Profile?> {
        return _profiles
            .map { list ->
                list.find { it.id == id }
            }
            .distinctUntilChanged()
    }

    override fun getAll(): Flow<List<Profile>> {
        return _profiles.asStateFlow()
    }

    override suspend fun create(profile: Profile) {
        _profiles.update { currentProfiles ->
            val newList = currentProfiles + profile
            newList
        }
    }

    override suspend fun update(profile: Profile) {
        _profiles.update { currentProfiles ->
            val newList = currentProfiles.filter { it.id != profile.id } + profile
            newList
        }
    }

    override suspend fun delete(id: ProfileId) {
        _profiles.update { currentProfiles ->
            val newList = currentProfiles.filter { it.id != id }
            newList
        }
    }

}