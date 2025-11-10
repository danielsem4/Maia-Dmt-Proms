package maia.dmt.market.presentation.entryInstructions

import androidx.compose.runtime.Composable
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_intro_title
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketEntryInstructionsRoot(
    viewModel: MarketEntryInstructionsViewModel = koinViewModel(),
) {


}

@Composable
fun MarketEntryInstructionsScreen(
    state: MarketEntryInstructionsState,
    onAction: (MarketEntryInstructionsAction) -> Unit,
) {

//    DmtBaseScreen(
//        titleText = stringResource(Res.string.cogTest_market_intro_title),
//        onIconClick = { },
//        content = {
//
//        }
}

@Composable
@Preview
fun MarketEntryInstructionsPreview() {
    DmtTheme {
        MarketEntryInstructionsRoot()
    }
}