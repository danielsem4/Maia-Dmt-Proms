package maia.dmt.hitber.presentation.hitberThiredQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberThiredQuestionRoot() {

}

@Composable
fun HitberThiredQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberThiredQuestionPreview() {
    DmtTheme {
        HitberThiredQuestionScreen()
    }
}
