package maia.dmt.pass.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call
import dmtproms.cogtest.pass.presentation.generated.resources.pass_phone
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.pass.presentation.model.AppUiModel
import maia.dmt.pass.presentation.passApps.AppType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppIconItem(
    modifier: Modifier = Modifier,
    app: AppUiModel,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(app.iconRes),
            contentDescription = stringResource(app.nameRes),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(app.color)
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(app.nameRes),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
@Preview
fun AppIconItemPreview() {
    DmtTheme {
         AppIconItem(
             modifier = Modifier,
             AppUiModel(
                 type = AppType.CALL,
                 nameRes = Res.string.cogTest_Pass_call,
                 iconRes = Res.drawable.pass_phone,
                 color = Color(0xFF26A69A)
             ),
             onClick = {}
         )
     }
}