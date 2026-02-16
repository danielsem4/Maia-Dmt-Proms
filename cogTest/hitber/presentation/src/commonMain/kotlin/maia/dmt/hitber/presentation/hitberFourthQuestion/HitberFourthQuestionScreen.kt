package maia.dmt.hitber.presentation.hitberFourthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberFourthQuestionRoot() {

}

@Composable
fun HitberFourthQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberFourthQuestionPreview() {
    DmtTheme {
        HitberFourthQuestionScreen()
    }
}
