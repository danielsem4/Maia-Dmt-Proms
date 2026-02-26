package maia.dmt.hitber.presentation.hitberSeventhQuestion.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_napkin
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_table
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.hitber.presentation.hitberSeventhQuestion.NapkinColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberTableWithNapkins(
    modifier: Modifier = Modifier,
    onNapkinPositioned: (NapkinColor, Rect) -> Unit
) {
    val tableAspectRatio = 1.5f

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        val screenAspectRatio = maxWidth / maxHeight

        val tableModifier = if (screenAspectRatio > tableAspectRatio) {
            Modifier.fillMaxSize().aspectRatio(tableAspectRatio)
        } else {
            Modifier.fillMaxWidth().aspectRatio(tableAspectRatio)
        }

        val tableHeight = if (screenAspectRatio > tableAspectRatio) maxHeight else maxWidth / tableAspectRatio
        val tableWidth = tableHeight * tableAspectRatio

        val napkinSize = tableWidth * 0.12f

        val verticalOffset = tableHeight * 0.08f

        Box(modifier = tableModifier) {
            Image(
                painter = painterResource(Res.drawable.hitber_table),
                contentDescription = "Table",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = verticalOffset),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NapkinView(NapkinColor.RED, napkinSize) { rect ->
                    onNapkinPositioned(NapkinColor.RED, rect)
                }
                NapkinView(NapkinColor.GREEN, napkinSize) { rect ->
                    onNapkinPositioned(NapkinColor.GREEN, rect)
                }
                NapkinView(NapkinColor.BLUE, napkinSize) { rect ->
                    onNapkinPositioned(NapkinColor.BLUE, rect)
                }
                NapkinView(NapkinColor.YELLOW, napkinSize) { rect ->
                    onNapkinPositioned(NapkinColor.YELLOW, rect)
                }
            }
        }
    }
}

@Composable
private fun NapkinView(
    color: NapkinColor,
    size: Dp,
    onPositioned: (Rect) -> Unit,
) {
    val tint = when (color) {
        NapkinColor.RED -> Color.Red
        NapkinColor.GREEN -> Color.Green
        NapkinColor.BLUE -> Color.Blue
        NapkinColor.YELLOW -> Color.Yellow
    }

    Image(
        painter = painterResource(Res.drawable.hitber_napkin),
        contentDescription = "${color.name} napkin",
        colorFilter = ColorFilter.tint(tint),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(size)
            .onGloballyPositioned { coords ->
                onPositioned(coords.boundsInRoot())
            },
    )
}
@Composable
@Preview
fun HitberTableWithNapkinsPreview() {
    DmtTheme {
        HitberTableWithNapkins(
            onNapkinPositioned = { _, _ -> }
        )
    }
}