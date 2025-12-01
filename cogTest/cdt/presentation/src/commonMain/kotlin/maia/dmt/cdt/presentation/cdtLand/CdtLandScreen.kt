package maia.dmt.cdt.presentation.cdtLand

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_gender_disclaimer
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_start
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_the_clock_test
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_title
import dmtproms.cogtest.cdt.presentation.generated.resources.hit_logo
import maia.dmt.cdt.presentation.components.AnimatedClock
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CdtLandRoot() {
    CdtLandScreen()
}

@Composable
fun CdtLandScreen() {

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_title),
        onIconClick = {  },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = stringResource(Res.string.cogTest_cdt_the_clock_test),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(8.dp))

                AnimatedClock()

                Spacer(modifier = Modifier.padding(8.dp))

                DmtButton(
                    text = stringResource(Res.string.cogTest_cdt_start),
                    onClick = { /* navigate to next screen */ },
                )

                Text(
                    text = stringResource(Res.string.cogTest_cdt_gender_disclaimer),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.weight(0.5f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(0.2f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "V 3.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                    Image(
                        painter = painterResource(Res.drawable.hit_logo),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun CdtLandPreview() {
    DmtTheme {
        CdtLandScreen()
    }
}