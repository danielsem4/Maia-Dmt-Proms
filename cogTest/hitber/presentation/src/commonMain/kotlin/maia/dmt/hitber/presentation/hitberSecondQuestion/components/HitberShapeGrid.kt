package maia.dmt.hitber.presentation.hitberSecondQuestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.shapeDraw.DmtShape
import maia.dmt.hitber.domain.model.HitberShape

@Composable
fun ShapeGrid(
    visibleShapes: List<HitberShape>,
    selectedShapes: Set<HitberShape>,
    itemsPerRow: Int,
    onShapeClick: (HitberShape) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rows = visibleShapes.chunked(itemsPerRow)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        rows.forEach { rowShapes ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowShapes.forEach { shape ->
                    ShapeItem(
                        shape = shape,
                        isSelected = shape in selectedShapes,
                        onClick = { onShapeClick(shape) },
                    )
                }
            }
        }
    }
}

@Composable
fun ShapeItem(
    shape: HitberShape,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .size(150.dp)
            .clip(cornerShape)
            .then(
                if (isSelected) {
                    Modifier.background(primaryColor, cornerShape)
                } else {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = cornerShape,
                    )
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        DmtShape(
            shapeKey = shape.registryKey,
            modifier = Modifier.size(80.dp),
            color = if (isSelected) Color.White else primaryColor,
            strokeWidth = 3.dp,
        )
    }
}
