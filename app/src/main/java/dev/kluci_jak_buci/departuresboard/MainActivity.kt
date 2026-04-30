package dev.kluci_jak_buci.departuresboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.Dashboard
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme
import dev.kluci_jak_buci.departuresboard.ui.screens.dashboard.dashboard
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.ProfileEditor
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.profileEditor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeparturesBoardTheme {
                App(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Dashboard,
        modifier = modifier,
        enterTransition = ScreenTransitions.enter,
        exitTransition = ScreenTransitions.exit,
        popEnterTransition = ScreenTransitions.popEnter,
        popExitTransition = ScreenTransitions.popExit,
    ) {
        dashboard(
            onAddDepartureClick = {
                navController.navigate(ProfileEditor)
            }
        )
        profileEditor(
            navController = navController,
            onBackArrowClick = { navController.popBackStack() }
        )

    }
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