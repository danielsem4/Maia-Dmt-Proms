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
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.pow
import kotlin.math.sqrt

data class BodyArea(
    val id: String,
    val position: Offset, // normalized [0..1]
    val radius: Float = 30f,
    val painOptions: List<String>
)

@Composable
fun DmtHumanBodyLayout(
    modifier: Modifier = Modifier,
    onPainAreasChanged: (Map<String, List<String>>) -> Unit = {}
) {
    var showingBack by remember { mutableStateOf(false) }
    var selectedArea by remember { mutableStateOf<BodyArea?>(null) }
    val painSelections = remember { mutableStateMapOf<String, List<String>>() }

    val frontAreas = remember {
        listOf(
            BodyArea("Head", Offset(0.5f, 0.12f), painOptions = listOf("Headache", "Migraine", "Dizziness", "Other")),
            BodyArea("Chest", Offset(0.5f, 0.32f), painOptions = listOf("Sharp pain", "Pressure", "Burning", "Other")),
            BodyArea("Belly", Offset(0.5f, 0.45f), painOptions = listOf("Cramps", "Bloating", "Sharp pain", "Other")),
            BodyArea("Left Shoulder", Offset(0.35f, 0.25f), painOptions = listOf("Stiffness", "Sharp pain", "Weakness", "Other")),
            BodyArea("Right Shoulder", Offset(0.65f, 0.25f), painOptions = listOf("Stiffness", "Sharp pain", "Weakness", "Other")),
            BodyArea("Left Arm", Offset(0.25f, 0.40f), painOptions = listOf("Numbness", "Tingling", "Sharp pain", "Other")),
            BodyArea("Right Arm", Offset(0.75f, 0.40f), painOptions = listOf("Numbness", "Tingling", "Sharp pain", "Other")),
            BodyArea("Left Knee", Offset(0.43f, 0.72f), painOptions = listOf("Swelling", "Stiffness", "Sharp pain", "Other")),
            BodyArea("Right Knee", Offset(0.57f, 0.72f), painOptions = listOf("Swelling", "Stiffness", "Sharp pain", "Other")),
            BodyArea("Left Foot", Offset(0.42f, 0.92f), painOptions = listOf("Swelling", "Numbness", "Sharp pain", "Other")),
            BodyArea("Right Foot", Offset(0.58f, 0.92f), painOptions = listOf("Swelling", "Numbness", "Sharp pain", "Other"))
        )
    }

    val backAreas = remember {
        listOf(
            BodyArea("Neck", Offset(0.5f, 0.18f), painOptions = listOf("Stiffness", "Sharp pain", "Limited mobility", "Other")),
            BodyArea("Upper Back", Offset(0.5f, 0.32f), painOptions = listOf("Muscle pain", "Stiffness", "Sharp pain", "Other")),
            BodyArea("Lower Back", Offset(0.5f, 0.50f), painOptions = listOf("Dull ache", "Sharp pain", "Radiating pain", "Other")),
            BodyArea("Buttocks", Offset(0.5f, 0.60f), painOptions = listOf("Muscle pain", "Numbness", "Sharp pain", "Other"))
        )
    }

    val currentAreas = if (showingBack) backAreas else frontAreas

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(10.dp))
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
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageWidth = constraints.maxWidth.toFloat()
                val imageHeight = constraints.maxHeight.toFloat()

                // Body Image
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

                // Touch overlay
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
                        modifier = Modifier.fillMaxWidth()
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
