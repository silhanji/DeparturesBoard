package dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoTransfer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun LineBadge(
    line: String?,
    modifier: Modifier = Modifier,
    small: Boolean = false,
) {
    val size = if(small) 48.dp else 64.dp
    val textStyle = if(small)
        MaterialTheme.typography.bodySmall
    else
        MaterialTheme.typography.headlineSmall


    Surface(
        shape = RoundedCornerShape(1000.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .width(size)
            .aspectRatio(1.0f),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if(line != null) {
                Text(
                    text = line,
                    style = textStyle,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.NoTransfer,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LineBadgePreview() {
    DeparturesBoardTheme {
        LineBadge(
            line = "C",
            small = false,
        )
    }
}