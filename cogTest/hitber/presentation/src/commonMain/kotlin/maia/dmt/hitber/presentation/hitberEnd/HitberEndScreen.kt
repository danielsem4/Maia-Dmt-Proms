package maia.dmt.hitber.presentation.hitberEnd

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberEndRoot() {

}

@Composable
fun HitberEndScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberEndPreview() {
    DmtTheme {
        HitberEndScreen()
    }
}
