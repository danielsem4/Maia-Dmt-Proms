package maia.dmt.hitber.presentation.hitberEntry

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberEntryRoot() {

}

@Composable
fun HitberEntryScreen() {

    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberEntryPreview() {
    DmtTheme {
        HitberEntryScreen()
    }
}