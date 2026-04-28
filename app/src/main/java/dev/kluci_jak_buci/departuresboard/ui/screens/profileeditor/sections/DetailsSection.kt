package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.ui.components.Field
import dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.InputField

@Composable
fun DetailsSection(
    name: InputField<String>,
    onNameChange: (String) -> Unit,
) {
    Section(name = stringResource(R.string.profile_details)) {
        Field(
            value = name.value,
            onValueChange = onNameChange,
            label = { Text(text = stringResource(R.string.name)) },
            isError = name.isError,
            supportingText = {
                name.errorMessage?.let { Text(text = it) }
            },
        )
    }
}

@Preview
@Composable
fun DetailsSectionPreview() {
    var name by remember { mutableStateOf(InputField("Work")) }

    DetailsSection(
        name = name,
        onNameChange = { name = InputField(it) },
    )
}