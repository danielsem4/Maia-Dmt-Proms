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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntSize
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.body_back_img
import dmtproms.core.designsystem.generated.resources.body_front_img
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.pow
import kotlin.math.sqrt

data class BodyArea(
    val id: String,
    val position: Offset,
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

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

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

    val frontAreaDefaults = listOf(
        BodyArea("Head", Offset(0.5f, 0.08f), painOptions = emptyList()),
        BodyArea("Chest", Offset(0.5f, 0.32f), painOptions = emptyList()),
        BodyArea("Belly", Offset(0.5f, 0.45f), painOptions = emptyList()),
        BodyArea("Left Shoulder", Offset(0.28f, 0.25f), painOptions = emptyList()),
        BodyArea("Right Shoulder", Offset(0.72f, 0.25f), painOptions = emptyList()),
        BodyArea("Left Arm", Offset(0.18f, 0.40f), painOptions = emptyList()),
        BodyArea("Right Arm", Offset(0.82f, 0.40f), painOptions = emptyList()),
        BodyArea("Left Knee", Offset(0.43f, 0.72f), painOptions = emptyList()),
        BodyArea("Right Knee", Offset(0.57f, 0.72f), painOptions = emptyList()),
        BodyArea("Left Foot", Offset(0.42f, 0.92f), painOptions = emptyList()),
        BodyArea("Right Foot", Offset(0.58f, 0.92f), painOptions = emptyList())
    )

    val backAreaDefaults = listOf(
        BodyArea("Neck", Offset(0.5f, 0.18f), painOptions = emptyList()),
        BodyArea("Upper Back", Offset(0.5f, 0.32f), painOptions = emptyList()),
        BodyArea("Lower Back", Offset(0.5f, 0.50f), painOptions = emptyList()),
        BodyArea("Buttocks", Offset(0.5f, 0.60f), painOptions = emptyList())
    )

    val frontAreas = parseAreaValues(frontAreasValues, frontAreaDefaults)
    val backAreas = parseAreaValues(backAreasValues, backAreaDefaults)

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 32.dp)
                    .background(
                        color = extendedColors.surfaceHigher,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
                    .onGloballyPositioned { coordinates ->
                        containerSize = coordinates.size
                    },
                contentAlignment = Alignment.Center
            ) {
                if (containerSize != IntSize.Zero) {
                    val imageAspectRatio = 0.42f

                    val containerWidth = with(density) { containerSize.width.toDp().value }
                    val containerHeight = with(density) { containerSize.height.toDp().value }
                    val containerAspectRatio = containerWidth / containerHeight

                    val (imageWidth, imageHeight, offsetX, offsetY) = if (containerAspectRatio > imageAspectRatio) {
                        val width = containerHeight * imageAspectRatio
                        val height = containerHeight
                        val xOffset = (containerWidth - width) / 2f
                        ImageBounds(width, height, xOffset, 0f)
                    } else {
                        val width = containerWidth
                        val height = containerWidth / imageAspectRatio
                        val yOffset = (containerHeight - height) / 2f
                        ImageBounds(width, height, 0f, yOffset)
                    }

                    val dotRadius = imageHeight * 0.02f

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                if (showingBack) Res.drawable.body_back_img
                                else Res.drawable.body_front_img
                            ),
                            contentDescription = if (showingBack) "Back of body" else "Front of body",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(currentAreas, imageWidth, imageHeight, offsetX, offsetY, dotRadius) {
                                    detectTapGestures { tapOffset ->
                                        val tapX = tapOffset.x / density.density
                                        val tapY = tapOffset.y / density.density

                                        val clickedArea = currentAreas.find { area ->
                                            val actualX = offsetX + (area.position.x * imageWidth)
                                            val actualY = offsetY + (area.position.y * imageHeight)

                                            val distance = sqrt(
                                                (tapX - actualX).pow(2) +
                                                        (tapY - actualY).pow(2)
                                            )
                                            distance <= dotRadius
                                        }
                                        if (clickedArea != null) selectedArea = clickedArea
                                    }
                                }
                        ) {
                            val primaryColor = Color(0xFF4CAF50)
                            val selectedColor = Color(0xFF2196F3)

                            currentAreas.forEach { area ->
                                if (area.painOptions.isNotEmpty()) {
                                    val x = offsetX + (area.position.x * imageWidth)
                                    val y = offsetY + (area.position.y * imageHeight)

                                    val position = Offset(
                                        x * density.density,
                                        y * density.density
                                    )

                                    val hasSelection = painSelections[area.id]?.isNotEmpty() == true
                                    val color = if (hasSelection) selectedColor else primaryColor

                                    val radiusPx = dotRadius * density.density
                                    drawCircle(color.copy(alpha = 0.4f), radiusPx, position)
                                    drawCircle(color.copy(alpha = 0.9f), radiusPx * 0.6f, position)
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedArea?.let { area ->
            DmtContentDialog(
                title = "${area.id} Pain",
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

// Helper data class for image bounds
private data class ImageBounds(
    val width: Float,
    val height: Float,
    val offsetX: Float,
    val offsetY: Float
)

@Preview
@Composable
fun DmtHumanBodyLayoutPreview() {
    DmtTheme {
        // Create sample data with all areas having pain options
        val frontAreasWithOptions = listOf(
            "Head (?) Headache, Migraine, Tension",
            "Chest (?) Sharp Pain, Burning, Pressure",
            "Belly (?) Cramping, Bloating, Sharp Pain",
            "Left Shoulder (?) Aching, Stiffness, Sharp Pain",
            "Right Shoulder (?) Aching, Stiffness, Sharp Pain",
            "Left Arm (?) Numbness, Tingling, Weakness",
            "Right Arm (?) Numbness, Tingling, Weakness",
            "Left Knee (?) Swelling, Stiffness, Aching",
            "Right Knee (?) Swelling, Stiffness, Aching",
            "Left Foot (?) Sharp Pain, Burning, Numbness",
            "Right Foot (?) Sharp Pain, Burning, Numbness"
        )

        val backAreasWithOptions = listOf(
            "Neck (?) Stiffness, Sharp Pain, Aching",
            "Upper Back (?) Tension, Sharp Pain, Burning",
            "Lower Back (?) Aching, Sharp Pain, Radiating Pain",
            "Buttocks (?) Aching, Numbness, Sharp Pain"
        )

        DmtHumanBodyLayout(
            frontAreasValues = frontAreasWithOptions,
            backAreasValues = backAreasWithOptions,
            initialSelections = mapOf(
                "Chest" to listOf("Sharp Pain"),
                "Lower Back" to listOf("Aching", "Sharp Pain")
            ),
            onPainAreasChanged = { selections ->
                println("Pain areas changed: $selections")
            }
        )
    }
}