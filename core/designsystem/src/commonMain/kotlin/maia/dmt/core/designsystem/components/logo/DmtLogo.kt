package maia.dmt.core.designsystem.components.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.app_logo_image

@Composable
fun DmtLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(Res.drawable.app_logo_image),
        contentDescription = null,
        modifier = modifier.size(100.dp)
    )
}
