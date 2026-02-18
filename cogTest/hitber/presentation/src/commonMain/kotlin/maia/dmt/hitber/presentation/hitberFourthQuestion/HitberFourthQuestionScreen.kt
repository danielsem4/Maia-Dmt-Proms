package maia.dmt.hitber.presentation.hitberFourthQuestion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_fourth_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_naming_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_ball
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_balloon
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_book
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_glasses
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_key
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_lemon
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_pencil
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_phone
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_ruler
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_shoe
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_table
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_watch
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
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

        ImageSection(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        WordSelectionGrid(
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
            ImageSection(
                state = state,
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
                WordSelectionGrid(
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
private fun ImageSection(
    state: HitberFourthQuestionState,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state.currentStep,
        transitionSpec = {
            (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                    (slideOutHorizontally { width -> -width } + fadeOut())
        },
        label = "stepTransition",
        modifier = modifier,
    ) { step ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            state.selectedWord?.let { word ->
                Text(
                    text = word.label(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            SubcomposeAsyncImage(
                model = state.currentImageUrl,
                contentDescription = "Question image ${step.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                success = {
                    SubcomposeAsyncImageContent()
                },
            )
        }
    }
}

@Composable
private fun WordSelectionGrid(
    options: List<HitberWord>,
    selectedWord: HitberWord?,
    columns: Int,
    onWordClick: (HitberWord) -> Unit,
) {
    val rows = options.chunked(columns)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                rowItems.forEach { word ->
                    val isSelected = word == selectedWord
                    DmtParagraphCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onWordClick(word) },
                        text = word.label(),
                        style = if (isSelected) DmtCardStyle.PRIMARY else DmtCardStyle.ELEVATED,
                        textSize = MaterialTheme.typography.bodySmall,
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HitberWord.label(): String = when (this) {
    HitberWord.PENCIL -> stringResource(Res.string.cogTest_hitber_word_pencil)
    HitberWord.RULER -> stringResource(Res.string.cogTest_hitber_word_ruler)
    HitberWord.TABLE -> stringResource(Res.string.cogTest_hitber_word_table)
    HitberWord.BALL -> stringResource(Res.string.cogTest_hitber_word_ball)
    HitberWord.BALLOON -> stringResource(Res.string.cogTest_hitber_word_balloon)
    HitberWord.LEMON -> stringResource(Res.string.cogTest_hitber_word_lemon)
    HitberWord.KEY -> stringResource(Res.string.cogTest_hitber_word_key)
    HitberWord.WATCH -> stringResource(Res.string.cogTest_hitber_word_watch)
    HitberWord.GLASSES -> stringResource(Res.string.cogTest_hitber_word_glasses)
    HitberWord.SHOE -> stringResource(Res.string.cogTest_hitber_word_shoe)
    HitberWord.PHONE -> stringResource(Res.string.cogTest_hitber_word_phone)
    HitberWord.BOOK -> stringResource(Res.string.cogTest_hitber_word_book)
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
