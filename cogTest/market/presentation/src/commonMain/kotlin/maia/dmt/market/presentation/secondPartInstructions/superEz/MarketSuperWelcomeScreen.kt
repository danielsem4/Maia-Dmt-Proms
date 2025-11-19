package maia.dmt.market.presentation.secondPartInstructions.superEz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_button_text
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_part_one_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_follow_instructions
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_quiet_env
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_second_mission_welcome_title
import dmtproms.cogtest.market.presentation.generated.resources.logo_super_easy
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MarketSuperWelcomeRoot(
    onNavigateBack: () -> Unit = {},
    onNavigateToInstructions: () -> Unit = {}
) {

    MarketSuperWelcomeScreen(
        onAction = { action ->
            when (action) {
                is MarketSuperWelcomeAction.OnNavigateBack -> onNavigateBack()
                is MarketSuperWelcomeAction.OnNavigateToSecondPartTestInstructions -> onNavigateToInstructions()
            }
        }
    )

}

@Composable
fun MarketSuperWelcomeScreen(
    onAction: (MarketSuperWelcomeAction) -> Unit = {}
) {

    val instructionsText = buildString {
        append("• ")
        append(stringResource(Res.string.cogTest_market_second_mission_follow_instructions))
        append("\n\n")
        append("• ")
        append(stringResource(Res.string.cogTest_market_second_mission_quiet_env))
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_part_one_title),
        onIconClick = { onAction(MarketSuperWelcomeAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = stringResource(Res.string.cogTest_market_second_mission_welcome_title),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Image(
                    painter = painterResource(Res.drawable.logo_super_easy),
                    contentDescription = "Super image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1f),
                    contentScale = ContentScale.Crop
                )

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
                    onClick = { onAction(MarketSuperWelcomeAction.OnNavigateToSecondPartTestInstructions) },
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun MarketSuperWelcomePreview() {
    DmtTheme {
        MarketSuperWelcomeScreen()
    }
}