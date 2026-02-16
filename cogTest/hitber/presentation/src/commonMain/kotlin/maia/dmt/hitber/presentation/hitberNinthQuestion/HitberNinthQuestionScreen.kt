package maia.dmt.hitber.presentation.hitberNinthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberNinthQuestionRoot() {

}

@Composable
fun HitberNinthQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberNinthQuestionPreview() {
    DmtTheme {
        HitberNinthQuestionScreen()
    }
}
