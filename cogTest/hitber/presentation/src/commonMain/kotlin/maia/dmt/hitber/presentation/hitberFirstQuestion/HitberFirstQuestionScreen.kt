package maia.dmt.hitber.presentation.hitberFirstQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberFirstQuestionRoot() {

}

@Composable
fun HitberFirstQuestionScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberFirstQuestionPreview() {
    DmtTheme {
        HitberFirstQuestionScreen()
    }
}
