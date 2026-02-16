package maia.dmt.hitber.presentation.hitberFifthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberFifthQuestionRoot() {

}

@Composable
fun HitberFifthQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberFifthQuestionPreview() {
    DmtTheme {
        HitberFifthQuestionScreen()
    }
}
