package dev.kluci_jak_buci.departuresboard.ui.screens.searchstation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SearchStationResult(
    val station: String
) : Parcelable