package maia.dmt.hitber.presentation.hitberSixthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberSixthQuestionRoot() {

}

@Composable
fun HitberSixthQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberSixthQuestionPreview() {
    DmtTheme {
        HitberSixthQuestionScreen()
    }
}
