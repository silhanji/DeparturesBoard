package dev.kluci_jak_buci.departuresboard.ui.screens.selectlines

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import dev.kluci_jak_buci.departuresboard.domain.model.LineName
import dev.kluci_jak_buci.departuresboard.domain.model.PlatformId
import dev.kluci_jak_buci.departuresboard.domain.model.SelectedLine

@Parcelize
@Serializable
data class SelectLinesResult(
    val lines: List<SelectedLineParcelable>
) : Parcelable

@Parcelize
@Serializable
data class SelectedLineParcelable(
    val line: String,
    val platform: String
) : Parcelable {
    fun toDomain() = SelectedLine(LineName(line), PlatformId(platform))
}

fun SelectedLine.toParcelable() = SelectedLineParcelable(line.value, platform.value)
