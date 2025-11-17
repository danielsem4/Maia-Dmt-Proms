package maia.dmt.market.presentation.secondPartInstructions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_first_mission_done_body
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_first_mission_done_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_button_text
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_part_one_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MarketSecondPartInstructionsRoot(
    onNavigateBack: () -> Unit = {},
    onStartMarketSecondTest: () -> Unit = {}
) {

    MarketSecondPartInstructionsScreen(
        onAction = { action ->
            when (action) {
                is MarketSecondPartInstructionsAction.OnNavigateBack -> onNavigateBack()
                is MarketSecondPartInstructionsAction.OnStartMarketSecondPartTest -> onStartMarketSecondTest()
            }
        }
    )

}

@Composable
fun MarketSecondPartInstructionsScreen(
    onAction: (MarketSecondPartInstructionsAction) -> Unit,
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_part_one_title),
        onIconClick = { onAction(MarketSecondPartInstructionsAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = stringResource(Res.string.cogTest_market_first_mission_done_title),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.padding(8.dp))

                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_market_first_mission_done_body),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    modifier = Modifier
                        .padding(),
                    text = stringResource(Res.string.cogTest_market_intro_button_text),
                    onClick = { onAction(MarketSecondPartInstructionsAction.OnStartMarketSecondPartTest) },
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun MarketSecondPartInstructionsPreview() {
    DmtTheme {
        MarketSecondPartInstructionsScreen(
            onAction = {}
        )
    }
}