package dev.kluci_jak_buci.departuresboard.data.local

import dev.kluci_jak_buci.departuresboard.data.local.db.DbProfile
import dev.kluci_jak_buci.departuresboard.data.local.db.DbProfileWithLines
import dev.kluci_jak_buci.departuresboard.data.local.db.DbSelectedLine
import dev.kluci_jak_buci.departuresboard.data.local.db.ProfileDao
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.Profile
import dev.kluci_jak_buci.departuresboard.domain.model.ProfileId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine
import dev.kluci_jak_buci.departuresboard.domain.model.TimeFilter
import dev.kluci_jak_buci.departuresboard.domain.model.VehicleFilter
import dev.kluci_jak_buci.departuresboard.domain.repository.ProfilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalProfilesRepository @Inject constructor(
    private val dao: ProfileDao
) : ProfilesRepository {

    override fun get(id: ProfileId): Flow<Profile?> {
        return dao.get(id.value)
            .map { dbProfile ->
                if(dbProfile != null)
                    toProfile(dbProfile)
                else
                    null
            }
            .distinctUntilChanged()
    }

    override fun getAll(): Flow<List<Profile>> {
        return dao.getAll()
            .map { profiles ->
                profiles.map { toProfile(it) }
            }
    }

    override suspend fun create(profile: Profile) {
        val (dbProfile, dbLines) = toDbProfile(profile)
        dao.insert(dbProfile, dbLines)
    }

    override suspend fun update(profile: Profile) {
        val (dbProfile, dbLines) = toDbProfile(profile)
        dao.deleteSelectedLines(dbProfile.id)
        dao.update(dbProfile)
        dao.insert(dbLines)
    }

    override suspend fun delete(id: ProfileId) {
        dao.delete(id.value)
    }

    private fun toProfile(dbProfile: DbProfileWithLines): Profile {
        return Profile(
            id = ProfileId(dbProfile.profile.id),
            name = dbProfile.profile.name,
            selectedLines = dbProfile.selectedLines.map{ toSelectedLine(it) },
            timeFilter = toTimeFilter(dbProfile.profile),
            vehicleFilter = toVehicleFilter(dbProfile.profile),
        )
    }

    private fun toSelectedLine(dbSelectedLine: DbSelectedLine): SelectedLine {
        return SelectedLine(
            line = LineName(dbSelectedLine.line),
            platform = PlatformId(dbSelectedLine.platform),
        )
    }

    private fun toTimeFilter(dbProfile: DbProfile): TimeFilter? {
        return if(
            dbProfile.timeFilterFrom != null &&
            dbProfile.timeFilterTo != null
        ) {
            TimeFilter(
                from = dbProfile.timeFilterFrom,
                to = dbProfile.timeFilterTo,
            )
        } else {
            null
        }
    }

    private fun toVehicleFilter(dbProfile: DbProfile): VehicleFilter? {
        return if(
            dbProfile.vehicleFilterAirConditioned != null ||
            dbProfile.vehicleFilterWheelChairAccessible != null
        ) {
            VehicleFilter(
                wheelChairAccessible = dbProfile.vehicleFilterWheelChairAccessible,
                airConditioned = dbProfile.vehicleFilterAirConditioned,
            )
        } else {
            null
        }
    }

    private fun toDbProfile(profile: Profile): Pair<DbProfile, List<DbSelectedLine>> {
        val dbProfile = DbProfile(
            id = profile.id.value,
            name = profile.name,
            timeFilterFrom = profile.timeFilter?.from,
            timeFilterTo = profile.timeFilter?.to,
            vehicleFilterWheelChairAccessible = profile.vehicleFilter?.wheelChairAccessible,
            vehicleFilterAirConditioned = profile.vehicleFilter?.airConditioned,
        )
        val dbLines = profile.selectedLines
            .map {
                DbSelectedLine(
                    profileId = profile.id.value,
                    line = it.line.value,
                    platform = it.platform.value,
                )
            }

        return Pair(dbProfile, dbLines)
    }

}