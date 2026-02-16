package maia.dmt.hitber.presentation.hitberEightQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberEightQuestionRoot() {

}

@Composable
fun HitberEightQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberEightQuestionPreview() {
    DmtTheme {
        HitberEightQuestionScreen()
    }
}
