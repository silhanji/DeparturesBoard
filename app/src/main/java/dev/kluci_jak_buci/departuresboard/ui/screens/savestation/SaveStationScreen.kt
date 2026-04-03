package dev.kluci_jak_buci.departuresboard.ui.screens.savestation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kluci_jak_buci.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun SaveStationScreen(
    onBackArrowClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SaveStationTopBar(onBackArrowClick)
        },
        floatingActionButton = {
            SaveStationButton(
                onClick = { /* TODO: */ }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            StationNameInput(
                name = "",
                onNameChange = { /* TODO */ }
            )
            Routes(
                routeTypes = "Trams",
                routes = listOf("17", "14", "11"),
                enabledRoutes = setOf("14"),
            )
            Routes(
                routeTypes = "Buses",
                routes = listOf("134", "124", "170"),
                enabledRoutes = setOf("134", "124"),
            )
            Routes(
                routeTypes = "Subway",
                routes = listOf("C"),
                enabledRoutes = setOf(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveStationTopBar(
    onBackArrowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LargeTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBackArrowClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                "Budějovická (B)",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun StationNameInput(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = name,
        onValueChange = { newValue -> onNameChange(newValue) },
        singleLine = true,
        placeholder = { Text("Station name") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun Routes(
    routeTypes: String,
    routes: List<String>,
    enabledRoutes: Set<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(routeTypes)
        LazyColumn {
            items(routes) { route ->
                RouteSwitch(
                    route = route,
                    enabled = enabledRoutes.contains(route),
                    onEnabledChange = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun RouteSwitch(
    route: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier.padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = route,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange,
            )
        }
    }
}

@Composable
fun SaveStationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null
            )
        },
        text = {
            Text("Create station")
        },
        modifier = modifier,
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SaveStationScreenPreview() {
    DeparturesBoardTheme {
        SaveStationScreen(
            onBackArrowClick = { }
        )
    }
}