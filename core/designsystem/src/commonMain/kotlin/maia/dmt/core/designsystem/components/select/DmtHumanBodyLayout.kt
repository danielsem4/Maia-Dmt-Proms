package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.body_back_img
import dmtproms.core.designsystem.generated.resources.body_front_img
import dmtproms.core.designsystem.generated.resources.body_part_belly
import dmtproms.core.designsystem.generated.resources.body_part_buttocks
import dmtproms.core.designsystem.generated.resources.body_part_chest
import dmtproms.core.designsystem.generated.resources.body_part_head
import dmtproms.core.designsystem.generated.resources.body_part_left_arm
import dmtproms.core.designsystem.generated.resources.body_part_left_foot
import dmtproms.core.designsystem.generated.resources.body_part_left_knee
import dmtproms.core.designsystem.generated.resources.body_part_left_shoulder
import dmtproms.core.designsystem.generated.resources.body_part_lower_back
import dmtproms.core.designsystem.generated.resources.body_part_neck
import dmtproms.core.designsystem.generated.resources.body_part_right_arm
import dmtproms.core.designsystem.generated.resources.body_part_right_foot
import dmtproms.core.designsystem.generated.resources.body_part_right_knee
import dmtproms.core.designsystem.generated.resources.body_part_right_shoulder
import dmtproms.core.designsystem.generated.resources.body_part_upper_back
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.pow
import kotlin.math.sqrt

data class BodyArea(
    val id: String,
    val nameRes: StringResource,
    val position: Offset,
    val radius: Float = 35f,
    val painOptions: List<String>
)

@Composable
fun DmtHumanBodyLayout(
    modifier: Modifier = Modifier,
    frontAreasValues: List<String> = emptyList(),
    backAreasValues: List<String> = emptyList(),
    initialSelections: Map<String, List<String>> = emptyMap(),
    onPainAreasChanged: (Map<String, List<String>>) -> Unit = {}
) {
    var showingBack by remember { mutableStateOf(false) }
    var selectedArea by remember { mutableStateOf<BodyArea?>(null) }

    val painSelections = remember {
        mutableStateMapOf<String, List<String>>().apply {
            putAll(initialSelections)
        }
    }

    val frontAreaDefaults = remember {
        listOf(
            BodyArea("Head", Res.string.body_part_head, Offset(0.5f, 0.12f), painOptions = emptyList()),
            BodyArea("Chest", Res.string.body_part_chest, Offset(0.5f, 0.32f), painOptions = emptyList()),
            BodyArea("Belly", Res.string.body_part_belly, Offset(0.5f, 0.45f), painOptions = emptyList()),
            BodyArea("Left Shoulder", Res.string.body_part_left_shoulder, Offset(0.35f, 0.25f), painOptions = emptyList()),
            BodyArea("Right Shoulder", Res.string.body_part_right_shoulder, Offset(0.65f, 0.25f), painOptions = emptyList()),
            BodyArea("Left Arm", Res.string.body_part_left_arm, Offset(0.25f, 0.40f), painOptions = emptyList()),
            BodyArea("Right Arm", Res.string.body_part_right_arm, Offset(0.75f, 0.40f), painOptions = emptyList()),
            BodyArea("Left Knee", Res.string.body_part_left_knee, Offset(0.43f, 0.72f), painOptions = emptyList()),
            BodyArea("Right Knee", Res.string.body_part_right_knee, Offset(0.57f, 0.72f), painOptions = emptyList()),
            BodyArea("Left Foot", Res.string.body_part_left_foot, Offset(0.42f, 0.92f), painOptions = emptyList()),
            BodyArea("Right Foot", Res.string.body_part_right_foot, Offset(0.58f, 0.92f), painOptions = emptyList())
        )
    }

    val backAreaDefaults = remember {
        listOf(
            BodyArea("Neck", Res.string.body_part_neck, Offset(0.5f, 0.18f), painOptions = emptyList()),
            BodyArea("Upper Back", Res.string.body_part_upper_back, Offset(0.5f, 0.32f), painOptions = emptyList()),
            BodyArea("Lower Back", Res.string.body_part_lower_back, Offset(0.5f, 0.50f), painOptions = emptyList()),
            BodyArea("Buttocks", Res.string.body_part_buttocks, Offset(0.5f, 0.60f), painOptions = emptyList())
        )
    }

    fun parseAreaValues(values: List<String>, predefinedAreas: List<BodyArea>): List<BodyArea> {
        return predefinedAreas.map { area ->
            val matchingValue = values.find { value ->
                value.split("(?)")[0].trim().contains(area.id, ignoreCase = true)
            }

            if (matchingValue != null) {
                val parts = matchingValue.split("(?)")
                val painOptions = if (parts.size >= 2) {
                    parts[1].trim().split(",").map { it.trim() }
                } else {
                    emptyList()
                }
                area.copy(painOptions = painOptions)
            } else {
                area
            }
        }
    }

    val frontAreas = remember(frontAreasValues) { parseAreaValues(frontAreasValues, frontAreaDefaults) }
    val backAreas = remember(backAreasValues) { parseAreaValues(backAreasValues, backAreaDefaults) }

    val currentAreas = if (showingBack) backAreas else frontAreas

    val extendedColors = MaterialTheme.colorScheme.extended

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = extendedColors.surfaceHigher, RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { showingBack = !showingBack },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = if (showingBack) "Show front" else "Show back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 32.dp)
                    .background(
                        color = extendedColors.surfaceHigher,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageWidth = constraints.maxWidth.toFloat()
                val imageHeight = constraints.maxHeight.toFloat()

                Image(
                    painter = painterResource(if (showingBack) Res.drawable.body_back_img else Res.drawable.body_front_img),
                    contentDescription = if (showingBack) "Back of body" else "Front of body",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Dots Layer
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val primaryColor = Color(0xFF4CAF50)
                    val selectedColor = Color(0xFF2196F3)

                    currentAreas.forEach { area ->
                        if (area.painOptions.isNotEmpty()) {
                            val position = Offset(
                                area.position.x * size.width,
                                area.position.y * size.height
                            )

                            val hasSelection = painSelections[area.id]?.isNotEmpty() == true
                            val color = if (hasSelection) selectedColor else primaryColor

                            drawCircle(color.copy(alpha = 0.4f), area.radius, position)
                            drawCircle(color.copy(alpha = 0.9f), area.radius * 0.6f, position)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentAreas, imageWidth, imageHeight) {
                            detectTapGestures { tapOffset ->
                                val clickedArea = currentAreas.find { area ->
                                    val actualPos = Offset(
                                        area.position.x * imageWidth,
                                        area.position.y * imageHeight
                                    )
                                    val distance = sqrt(
                                        (tapOffset.x - actualPos.x).pow(2) + (tapOffset.y - actualPos.y).pow(2)
                                    )
                                    distance <= area.radius
                                }
                                if (clickedArea != null) selectedArea = clickedArea
                            }
                        }
                )
            }
        }

        selectedArea?.let { area ->
            DmtContentDialog(
                title = stringResource(area.nameRes),
                onDismiss = { selectedArea = null },
                content = {
                    val currentSelections = painSelections[area.id] ?: emptyList()
                    val checkboxOptions = area.painOptions.map {
                        CheckboxOption(it, currentSelections.contains(it))
                    }

                    DmtCheckboxCardGroup(
                        options = checkboxOptions,
                        onCheckedChange = { checked ->
                            if (checked.isEmpty()) painSelections.remove(area.id)
                            else painSelections[area.id] = checked
                            onPainAreasChanged(painSelections.toMap())
                        },
                        allowMultiple = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun DmtHumanBodyLayoutPreview() {
    DmtTheme {
        DmtHumanBodyLayout()
    }
}