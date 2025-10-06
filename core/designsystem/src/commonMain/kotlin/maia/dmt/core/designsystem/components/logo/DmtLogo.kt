package maia.dmt.core.designsystem.components.logo

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.app_logo
import dmtproms.core.designsystem.generated.resources.upload_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DmtLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.app_logo),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}