package maia.dmt.hitber.presentation.hitberFourthQuestion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_fourth_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_naming_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.hitber.presentation.hitberFourthQuestion.components.HitberImageSection
import maia.dmt.hitber.presentation.hitberFourthQuestion.components.HitberWordSelectionGrid
import maia.dmt.hitber.presentation.hitberFourthQuestion.components.label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberFourthQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberFourthQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberFourthQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberFourthQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HitberFourthQuestionScreen(
    state: HitberFourthQuestionState,
    onAction: (HitberFourthQuestionAction) -> Unit,
) {
    val deviceConfig = currentDeviceConfiguration()
    val isLandscape = deviceConfig == DeviceConfiguration.MOBILE_LANDSCAPE ||
            deviceConfig == DeviceConfiguration.TABLET_LANDSCAPE ||
            deviceConfig == DeviceConfiguration.DESKTOP

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_naming_title),
        onIconClick = {},
        content = {
            if (isLandscape) {
                LandscapeContent(state = state, onAction = onAction)
            } else {
                PortraitContent(state = state, onAction = onAction)
            }
        }
    )
}

@Composable
private fun PortraitContent(
    state: HitberFourthQuestionState,
    onAction: (HitberFourthQuestionAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.cogTest_hitber_fourth_mission_instructions),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        HitberImageSection(
            currentStep = state.currentStep,
            imageUrl = state.currentImageUrl,
            selectedWordLabel = state.selectedWord?.label(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        HitberWordSelectionGrid(
            options = state.options,
            selectedWord = state.selectedWord,
            columns = 4,
            onWordClick = { word ->
                onAction(HitberFourthQuestionAction.OnWordSelected(word))
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        DmtButton(
            text = stringResource(Res.string.cogTest_hitber_next),
            onClick = { onAction(HitberFourthQuestionAction.OnNextClick) },
            style = DmtButtonStyle.PRIMARY,
            enabled = state.selectedWord != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )
    }
}

@Composable
private fun LandscapeContent(
    state: HitberFourthQuestionState,
    onAction: (HitberFourthQuestionAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HitberImageSection(
                currentStep = state.currentStep,
                imageUrl = state.currentImageUrl,
                selectedWordLabel = state.selectedWord?.label(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_hitber_fourth_mission_instructions),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                )
                HitberWordSelectionGrid(
                    options = state.options,
                    selectedWord = state.selectedWord,
                    columns = 4,
                    onWordClick = { word ->
                        onAction(HitberFourthQuestionAction.OnWordSelected(word))
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DmtButton(
            text = stringResource(Res.string.cogTest_hitber_next),
            onClick = { onAction(HitberFourthQuestionAction.OnNextClick) },
            style = DmtButtonStyle.PRIMARY,
            enabled = state.selectedWord != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )
    }
}

@Composable
@Preview
fun HitberFourthQuestionPreview() {
    DmtTheme {
        HitberFourthQuestionScreen(
            state = HitberFourthQuestionState(
                options = listOf(
                    HitberWord.PENCIL, HitberWord.RULER, HitberWord.TABLE, HitberWord.BALL,
                    HitberWord.BALLOON, HitberWord.LEMON, HitberWord.KEY, HitberWord.WATCH,
                ),
            ),
            onAction = {},
        )
    }
}
