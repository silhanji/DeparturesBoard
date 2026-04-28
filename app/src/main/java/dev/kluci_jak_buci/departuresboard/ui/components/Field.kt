package dev.kluci_jak_buci.departuresboard.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Wrapper over OutlinedTextField which sets common app input style
 */
@Composable
fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label:  @Composable (() -> Unit)? = null,
    placeholder:  @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    readOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        isError = isError,
        supportingText = supportingText,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun FieldPreview() {
    Field(
        value = "Malostranska",
        onValueChange = TODO(),
        modifier = TODO(),
        label = TODO(),
        placeholder = TODO(),
        leadingIcon = TODO(),
        supportingText = TODO(),
        isError = TODO(),
        readOnly = TODO()
    )
}