package maia.dmt.core.designsystem.components.textFields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtParagraphTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 2,
    maxLines: Int = 6,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    BasicTextField(
        state = state,
        enabled = enabled,
        interactionSource = interactionSource,
        lineLimits = TextFieldLineLimits.MultiLine(
            minHeightInLines = minLines,
            maxHeightInLines = maxLines
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.extended.textPlaceholder
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        modifier = modifier,
        decorator = { innerBox ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = when {
                            isError -> MaterialTheme.colorScheme.error
                            isFocused -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (state.text.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                innerBox()
            }
        }
    )
}

@Composable
@Preview
fun DmtParagraphTextFieldPreview() {
    DmtTheme {
        DmtParagraphTextField(
            state = rememberTextFieldState(initialText = ""),
            modifier = Modifier.width(400.dp),
            placeholder = "Enter your text here...\nYou can write multiple lines."
        )
    }
}

@Composable
@Preview
fun DmtParagraphTextFieldWithTextPreview() {
    DmtTheme {
        DmtParagraphTextField(
            state = rememberTextFieldState(
                initialText = "This is a sample paragraph text field.\nIt supports multiple lines of text."
            ),
            modifier = Modifier.width(400.dp),
            placeholder = "Enter your text here..."
        )
    }
}