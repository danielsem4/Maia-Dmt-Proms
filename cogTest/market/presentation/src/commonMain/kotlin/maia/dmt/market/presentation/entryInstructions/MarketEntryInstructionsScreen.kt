package maia.dmt.market.presentation.entryInstructions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_button_text
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_description
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_part_one_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MarketEntryInstructionsRoot(
    onNavigateBack: () -> Unit = {},
    onStartMarketTest: () -> Unit = {}
) {
    MarketEntryInstructionsScreen(
        onAction = { action ->
            when (action) {
                is MarketEntryInstructionsAction.OnNavigateBack -> onNavigateBack()
                is MarketEntryInstructionsAction.OnStartMarketTest -> onStartMarketTest()
            }
        }
    )
}

@Composable
fun MarketEntryInstructionsScreen(
    onAction: (MarketEntryInstructionsAction) -> Unit,
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_part_one_title),
        onIconClick = { onAction(MarketEntryInstructionsAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = stringResource(Res.string.cogTest_market_intro_title),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                )
                Spacer(modifier = Modifier.weight(1f))

                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_market_intro_description),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    modifier = Modifier
                        .padding(),
                    text = stringResource(Res.string.cogTest_market_intro_button_text),
                    onClick = { onAction(MarketEntryInstructionsAction.OnStartMarketTest) },
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun MarketEntryInstructionsPreview() {
    DmtTheme {
        MarketEntryInstructionsScreen(
            onAction = {}
        )
    }
}