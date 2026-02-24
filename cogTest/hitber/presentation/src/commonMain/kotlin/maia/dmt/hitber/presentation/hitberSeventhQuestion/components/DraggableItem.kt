package maia.dmt.hitber.presentation.hitberSeventhQuestion.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_can
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_chicken
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_gil
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_grape
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_juice
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_koteg
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_milk
import maia.dmt.hitber.presentation.hitberSeventhQuestion.FridgeItem
import maia.dmt.hitber.presentation.hitberSeventhQuestion.FridgeItemType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

val ITEM_SIZE_DP = 72.dp

@Composable
fun DraggableItem(
    item: FridgeItem,
    isFridgeOpen: Boolean,
    onDrag: (dragAmount: Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha = if (item.isInFridge && !isFridgeOpen) 0f else 1f

    Image(
        painter = painterResource(item.type.toDrawable()),
        contentDescription = item.type.name,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(ITEM_SIZE_DP)
            .alpha(alpha)
            .pointerInput(item.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = { onDragEnd() },
                )
            },
    )
}

private fun FridgeItemType.toDrawable(): DrawableResource = when (this) {
    FridgeItemType.CAN -> Res.drawable.hitber_can
    FridgeItemType.GIL -> Res.drawable.hitber_gil
    FridgeItemType.MILK -> Res.drawable.hitber_milk
    FridgeItemType.KOTEG -> Res.drawable.hitber_koteg
    FridgeItemType.CHICKEN -> Res.drawable.hitber_chicken
    FridgeItemType.JUICE -> Res.drawable.hitber_juice
    FridgeItemType.GRAPE -> Res.drawable.hitber_grape
}
