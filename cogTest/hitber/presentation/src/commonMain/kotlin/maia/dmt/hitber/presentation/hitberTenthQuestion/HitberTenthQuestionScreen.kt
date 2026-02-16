package maia.dmt.hitber.presentation.hitberTenthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberTenthQuestionRoot() {

}

@Composable
fun HitberTenthQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberTenthQuestionPreview() {
    DmtTheme {
        HitberTenthQuestionScreen()
    }
}
