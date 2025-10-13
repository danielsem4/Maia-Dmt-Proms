package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.body_back
import dmtproms.core.designsystem.generated.resources.body_front
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.pow
import kotlin.math.sqrt

data class BodyArea(
    val id: String,
    val position: Offset,
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
            .background(
                shape = RoundedCornerShape(10.dp),
                color = Color.White
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Rotate button
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

            // Body image with clickable dots
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Body image
                Image(
                    painter = painterResource(if (showingBack) Res.drawable.body_back else Res.drawable.body_front),
                    contentDescription = if (showingBack) "Back of body" else "Front of body",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentAreas) {
                            detectTapGestures { tapOffset ->
                                val width = size.width.toFloat()
                                val height = size.height.toFloat()

                                currentAreas.forEach { area ->
                                    val actualPosition = Offset(
                                        area.position.x * width,
                                        area.position.y * height
                                    )

                                    val distance = sqrt(
                                        (tapOffset.x - actualPosition.x).pow(2) +
                                                (tapOffset.y - actualPosition.y).pow(2)
                                    )

                                    if (distance <= area.radius) {
                                        selectedArea = area
                                    }
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val primaryColor = Color(0xFF4CAF50)
                    val selectedColor = Color(0xFF2196F3)

                    currentAreas.forEach { area ->
                        val actualPosition = Offset(
                            area.position.x * width,
                            area.position.y * height
                        )

                        val hasSelection = painSelections.containsKey(area.id) &&
                                painSelections[area.id]?.isNotEmpty() == true

                        drawCircle(
                            color = if (hasSelection) selectedColor else primaryColor,
                            radius = area.radius,
                            center = actualPosition,
                            alpha = 0.4f
                        )

                        drawCircle(
                            color = if (hasSelection) selectedColor else primaryColor,
                            radius = area.radius * 0.6f,
                            center = actualPosition,
                            alpha = 0.9f
                        )
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
                    val checkboxOptions = area.painOptions.map { option ->
                        CheckboxOption(
                            text = option,
                            isChecked = currentSelections.contains(option)
                        )
                    }

                    DmtCheckboxCardGroup(
                        options = checkboxOptions,
                        onCheckedChange = { checkedValues ->
                            if (checkedValues.isEmpty()) {
                                painSelections.remove(area.id)
                            } else {
                                painSelections[area.id] = checkedValues
                            }
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

@Composable
@Preview
fun DmtHumanBodyLayoutPreview() {
    DmtTheme {
        DmtHumanBodyLayout(
            onPainAreasChanged = { selections ->
                println("Pain selections: $selections")
            }
        )
    }
}