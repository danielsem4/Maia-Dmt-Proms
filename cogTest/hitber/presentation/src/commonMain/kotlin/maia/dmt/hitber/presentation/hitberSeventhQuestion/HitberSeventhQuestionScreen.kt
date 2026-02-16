package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberSeventhQuestionRoot() {

}

@Composable
fun HitberSeventhQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberSeventhQuestionPreview() {
    DmtTheme {
        HitberSeventhQuestionScreen()
    }
}
