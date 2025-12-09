package maia.dmt.cdt.presentation.cdtGrade

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_circle_completeness
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_confirm
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_hands_presence_position
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_instruction
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_numbers_presence_sequence
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.select.DmtDropDown
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtGradeRoot(
    onNavigateBack: () -> Unit,
    viewModel: CdtGradeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.events.collect { event ->
            when (event) {
                is CdtGradeEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    CdtGradeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CdtGradeScreen(
    state: CdtGradeState,
    onAction: (CdtGradeAction) -> Unit
) {
    DmtBaseScreen(
        titleText = "Grade Clock",
        onIconClick = { onAction(CdtGradeAction.OnSaveClick) },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.clockBitmap != null) {
                        Image(
                            bitmap = state.clockBitmap,
                            contentDescription = "Clock Drawing",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    } else {
                        Text("No Drawing Found", color = Color.Gray)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.cogTest_cdt_instruction),
                        style = MaterialTheme.typography.titleLarge
                    )

                    GradeSection(
                        title = stringResource(Res.string.cogTest_cdt_circle_completeness),
                        options = state.circleOptions,
                        selected = state.selectedCircleGrade,
                        onSelect = { onAction(CdtGradeAction.OnCircleGradeSelected(it)) }
                    )

                    GradeSection(
                        title = stringResource(Res.string.cogTest_cdt_numbers_presence_sequence),
                        options = state.numbersOptions,
                        selected = state.selectedNumbersGrade,
                        onSelect = { onAction(CdtGradeAction.OnNumbersGradeSelected(it)) }
                    )

                    GradeSection(
                        title = stringResource(Res.string.cogTest_cdt_hands_presence_position),
                        options = state.handsOptions,
                        selected = state.selectedHandsGrade,
                        onSelect = { onAction(CdtGradeAction.OnHandsGradeSelected(it)) }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    DmtButton(
                        text = stringResource(Res.string.cogTest_cdt_confirm),
                        onClick = { onAction(CdtGradeAction.OnSaveClick) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Composable
private fun GradeSection(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        DmtDropDown(
            items = options,
            selectedItem = selected,
            onItemSelected = onSelect,
            placeholder = title,
            itemContent = { Text(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}