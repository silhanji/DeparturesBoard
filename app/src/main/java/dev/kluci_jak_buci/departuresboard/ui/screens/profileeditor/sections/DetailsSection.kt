package dev.kluci_jak_buci.departuresboard.ui.screens.profileeditor.sections

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.kluci_jak_buci.departuresboard.R
import dev.kluci_jak_buci.departuresboard.ui.components.Field

@Composable
fun DetailsSection(
    name: String,
    onNameChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String?
) {
    Section(name = stringResource(R.string.profile_details)) {
        Field(
            value = name,
            onValueChange = onNameChange,
            label = { Text(text = stringResource(R.string.name)) },
            isError = isError,
            supportingText = {
                errorMessage?.let { Text(text = it) }
            },
        )
    }
}