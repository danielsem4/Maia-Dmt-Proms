package maia.dmt.hitber.presentation.hitberSecondQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberSecondQuestionRoot() {

}

@Composable
fun HitberSecondQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberSecondQuestionPreview() {
    DmtTheme {
        HitberSecondQuestionScreen()
    }
}
