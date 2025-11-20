package maia.dmt.market.presentation.secondPartInstructions.secondPartTestInstructions

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
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_button_text
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_part_one_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_donation_items
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_find_items
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_shopping_task
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_title_instructions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MarketSecondPartTestInstructionsRoot(
    onNavigateBack: () -> Unit = {},
    onStartSecondPartTest: () -> Unit = {}
) {

    MarketSecondPartTestInstructionsScreen(
        onAction = { action ->
            when (action) {
                is MarketSecondPartTestInstructionsAction.OnNavigateBack -> onNavigateBack()
                is MarketSecondPartTestInstructionsAction.OnNavigateToSecondPartTest -> onStartSecondPartTest()
            }
        }
    )
}

@Composable
fun MarketSecondPartTestInstructionsScreen(
    onAction: (MarketSecondPartTestInstructionsAction) -> Unit,
) {
    val instructionsText = buildString {
        append("• ")
        append(stringResource(Res.string.cogTest_market_second_mission_shopping_task))
        append("\n\n")
        append("• ")
        append(stringResource(Res.string.cogTest_market_second_mission_find_items))
        append("\n\n")
        append("• ")
        append(stringResource(Res.string.cogTest_market_second_mission_donation_items))
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_part_one_title),
        onIconClick = { onAction(MarketSecondPartTestInstructionsAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = stringResource(Res.string.cogTest_market_second_mission_title_instructions),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = instructionsText,
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    modifier = Modifier
                        .padding(),
                    text = stringResource(Res.string.cogTest_market_intro_button_text),
                    onClick = { onAction(MarketSecondPartTestInstructionsAction.OnNavigateToSecondPartTest) },
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun SecondPartTestInstructionsPreview() {
    DmtTheme {
        MarketSecondPartTestInstructionsScreen(
            onAction = {}
        )
    }
}