package dev.kluci_jak_buci.departuresboard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Allows setting a list of string values into a single field.
 * Each item is displayed on its own line.
 *
 * The field is readonly and reacts only on user clicking on it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiLineClickableField(
    lines: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 3,
    label:  @Composable (() -> Unit)? = null,
    placeholder:  @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textValue = lines.joinToString(" • ") { it }

    Box(modifier = modifier) {
        BasicTextField(
            value = textValue,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = textValue,
                    innerTextField = {
                        if (lines.isEmpty()) {
                            innerTextField()
                        } else {
                            Column(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                lines.take(maxLines).forEach { line ->
                                    Text(
                                        text = line,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                if (lines.size > maxLines) {
                                    Text(
                                        text = "+${lines.size - maxLines} more",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    enabled = enabled,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                )
            }
        )
        // Clickable overlay to ensure click is always registered
        Box(
            Modifier
                .matchParentSize()
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .clickable(enabled = enabled) { onClick() }
        )
    }
}