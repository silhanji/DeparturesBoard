package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

sealed class Section(
    val icon: ImageVector,
    val title: String,
) {
    object CurrentProfiles : Section(
        icon = Icons.Default.PinDrop,
        title = "Aktualni odjezdy",
    )

    object SavedProfiles : Section(
        icon = Icons.Default.Bookmark,
        title = "Ulozene odjezdy",
    )
}


@Composable
fun Section(
    section: Section,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { },
) {
    Column(modifier = modifier.padding(bottom = 32.dp)) {
        SectionLabel(section)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SectionLabel(
    section: Section,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = section.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = section.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SectionPreview() {
    DeparturesBoardTheme {
        Section(Section.CurrentProfiles) {
            Box(
                modifier = Modifier.size(256.dp)
                    .background(Color.LightGray)
            )
        }
    }
}