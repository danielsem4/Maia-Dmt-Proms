package maia.dmt.core.designsystem.components.toast

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.upload_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ToastType {
    Normal, Success, Info, Warning, Error
}

@Composable
fun DmtToastView(
    message: String,
    type: ToastType = ToastType.Normal,
    backgroundColor: Color? = null,
) {
    val resolvedColor = backgroundColor ?: when (type) {
        ToastType.Normal -> Color.DarkGray
        ToastType.Success -> Color(0xFF4CAF50)
        ToastType.Info -> Color(0xFF2196F3)
        ToastType.Warning -> Color(0xFFFFC107)
        ToastType.Error -> Color(0xFFF44336)
    }

    val resolvedIcon = when (type) {
        ToastType.Success -> Res.drawable.upload_icon
        ToastType.Info -> Res.drawable.upload_icon
        ToastType.Warning -> Res.drawable.upload_icon
        ToastType.Error -> Res.drawable.upload_icon
        else -> Res.drawable.upload_icon
    }

    Surface(
        shape = RoundedCornerShape(56.dp),
        color = resolvedColor,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
            .defaultMinSize(minWidth = 120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(resolvedIcon),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(60.dp)
                    .padding(6.dp)
            )

            Text(
                text = message,
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
@Preview

fun DmtToastViewPreview() {
    DmtTheme {
         DmtToastView(
             message = "This is a toast message",
             type = ToastType.Success
         )
     }
}