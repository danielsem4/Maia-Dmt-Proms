package maia.dmt.orientation.presentation.seasons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.autumn_image
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_autumn
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_back_to_task
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_seasons_instructions_app_trial
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_seasons_instructions_app_trial2
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_seasons_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_spring
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_summer
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_winter
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_you_in_the_autumn
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_you_in_the_spring
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_you_in_the_summer
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_you_in_the_winter
import dmtproms.cogtest.orientation.presentation.generated.resources.spring_image
import dmtproms.cogtest.orientation.presentation.generated.resources.summer_image
import dmtproms.cogtest.orientation.presentation.generated.resources.winter_image
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.orientation.domain.model.Season
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SeasonsSelectionOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SeasonsSelectionOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is SeasonsSelectionOrientationEvent.NavigateToNext -> onNavigateToNext()
                is SeasonsSelectionOrientationEvent.NavigateBack -> onNavigateBack()
                is SeasonsSelectionOrientationEvent.ShowError -> {

                }
            }
        }
    }

    SeasonsSelectionOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SeasonsSelectionOrientationScreen(
    state: SeasonsSelectionOrientationState,
    onAction: (SeasonsSelectionOrientationAction) -> Unit
) {
    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "title",
            description = "description",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(SeasonsSelectionOrientationAction.OnBackToTask) },
            onDismiss = { onAction(SeasonsSelectionOrientationAction.OnDismissInactivityDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_seasons_title),
        onIconClick = { onAction(SeasonsSelectionOrientationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SeasonButton(
                                season = Season.WINTER,
                                label = Res.string.cog_orientation_winter,
                                isSelected = state.selectedSeason == Season.WINTER,
                                onAction = onAction
                            )
                            SeasonButton(
                                season = Season.SPRING,
                                label = Res.string.cog_orientation_spring,
                                isSelected = state.selectedSeason == Season.SPRING,
                                onAction = onAction
                            )
                            SeasonButton(
                                season = Season.SUMMER,
                                label = Res.string.cog_orientation_summer,
                                isSelected = state.selectedSeason == Season.SUMMER,
                                onAction = onAction
                            )
                            SeasonButton(
                                season = Season.AUTUMN,
                                label = Res.string.cog_orientation_autumn,
                                isSelected = state.selectedSeason == Season.AUTUMN,
                                onAction = onAction
                            )
                        }
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            painter = painterResource(getSeasonImage(state.selectedSeason)),
                            contentDescription = "Season"
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.padding(8.dp))

                        DmtParagraphCard(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(
                                if (state.isFirstRound) {
                                    Res.string.cog_orientation_seasons_instructions_app_trial
                                } else {
                                    Res.string.cog_orientation_seasons_instructions_app_trial2
                                }
                            ),
                        )

                        DmtParagraphCard(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(getSeasonText(state.selectedSeason)),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        DmtButton(
                            modifier = Modifier.padding(),
                            text = stringResource(Res.string.cog_orientation_next),
                            onClick = { onAction(SeasonsSelectionOrientationAction.OnNextClick) },
                            enabled = state.selectedSeason != null
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun SeasonButton(
    season: Season,
    label: StringResource,
    isSelected: Boolean,
    onAction: (SeasonsSelectionOrientationAction) -> Unit
) {
    DmtButton(
        modifier = Modifier
            .padding(4.dp)
            .widthIn(max = 120.dp),
        text = stringResource(label),
        onClick = { onAction(SeasonsSelectionOrientationAction.OnSeasonSelected(season)) },
        style = if (isSelected) DmtButtonStyle.PRIMARY else DmtButtonStyle.SECONDARY
    )
}

private fun getSeasonImage(season: Season?): DrawableResource {
    return when (season) {
        Season.WINTER -> Res.drawable.winter_image
        Season.SPRING -> Res.drawable.spring_image
        Season.SUMMER -> Res.drawable.summer_image
        Season.AUTUMN -> Res.drawable.autumn_image
        null -> Res.drawable.autumn_image
    }
}

private fun getSeasonText(season: Season?): StringResource {
    return when (season) {
        Season.WINTER -> Res.string.cog_orientation_you_in_the_winter
        Season.SPRING -> Res.string.cog_orientation_you_in_the_spring
        Season.SUMMER -> Res.string.cog_orientation_you_in_the_summer
        Season.AUTUMN -> Res.string.cog_orientation_you_in_the_autumn
        null -> Res.string.cog_orientation_you_in_the_autumn
    }
}

@Composable
@Preview
fun SeasonsSelectionOrientationPreview() {
    DmtTheme {
        SeasonsSelectionOrientationScreen(
            state = SeasonsSelectionOrientationState(
                selectedSeason = Season.AUTUMN,
                isFirstRound = true
            ),
            onAction = {}
        )
    }
}