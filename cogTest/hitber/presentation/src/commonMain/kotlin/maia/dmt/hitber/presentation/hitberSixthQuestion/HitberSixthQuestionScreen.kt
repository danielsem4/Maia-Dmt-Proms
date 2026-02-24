package maia.dmt.hitber.presentation.hitberSixthQuestion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberSixthQuestionRoot() {

}

@Composable
fun HitberSixthQuestionScreen() {

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_hitber_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                DmtButton(
                    text = stringResource(Res.string.cogTest_hitber_next),
                    onClick = {},
                )
            }
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
