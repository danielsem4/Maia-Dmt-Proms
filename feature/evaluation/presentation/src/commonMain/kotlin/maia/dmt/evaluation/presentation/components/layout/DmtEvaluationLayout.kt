package maia.dmt.evaluation.presentation.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.clearFocusOnTap
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtEvaluationLayout(
    modifier: Modifier = Modifier,
    title: String = "How do you feel?",
    onPrevClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    prevButtonText: String = "Prev",
    nextButtonText: String = "Next",
    isLoading: Boolean = false,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DmtButton(
                text = prevButtonText,
                onClick = onPrevClick,
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                style = DmtButtonStyle.PRIMARY
            )

            DmtButton(
                text = nextButtonText,
                onClick = onNextClick,
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                style = DmtButtonStyle.PRIMARY,
                isLoading = isLoading
            )
        }
    }
}

@Composable
@Preview
fun DmtEvaluationLayoutPreview() {
    DmtTheme {
        DmtEvaluationLayout {
            Text(
                text = "answer here",
                fontSize = 24.sp,
                color = Color.Gray
            )
        }
    }
}