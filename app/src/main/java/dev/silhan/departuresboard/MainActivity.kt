package dev.silhan.departuresboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.silhan.departuresboard.ui.theme.DeparturesBoardTheme
import dev.silhan.departuresboard.ui.screens.dashboard.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeparturesBoardTheme {
                App(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    DashboardScreen(modifier)
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AppPreview() {
    DeparturesBoardTheme {
        App()
    }
}