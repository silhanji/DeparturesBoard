package dev.silhan.departuresboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.silhan.departuresboard.ui.theme.DeparturesBoardTheme

@Composable
fun ChooseStationScreen(
    onBackNavigation: () -> Unit,
) {
    Scaffold(
        topBar = {
            ChooseStationTopBar(onBackNavigation = onBackNavigation)
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchInput(
                search = "", // TODO
                onSearchChange = { /* TODO */ }
            )
            FoundStations(
                stations = listOf(
                    "Háje", "Opatov", "Chodov", "Roztyly", "Kačerov", "Budějovická", "Pankrác",
                    "Pražského povstání", "Vyšehrad", "I.P. Pavlova", "Hlavní nádraží", "Florenc"
                ),
                onStationSelected = { /* TODO: */ },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStationTopBar(
    onBackNavigation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LargeTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBackNavigation() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                "New station",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun SearchInput(
    search: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = search,
        onValueChange = { newValue -> onSearchChange(newValue) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        singleLine = true,
        placeholder = { Text("Station name") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun FoundStations(
    stations: List<String>,
    onStationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        "Available stations",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
    )
    LazyColumn(modifier = modifier) {
        items(stations) { station ->
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, 2.dp)
                    .clickable(
                        onClick = { onStationSelected(station) }
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        station,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ChooseStationScreenPreview() {
    DeparturesBoardTheme {
        ChooseStationScreen(
            onBackNavigation = { }
        )
    }
}