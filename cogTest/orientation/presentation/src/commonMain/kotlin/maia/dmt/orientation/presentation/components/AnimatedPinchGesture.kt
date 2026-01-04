package maia.dmt.orientation.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AnimatedPinchGesture(
    modifier: Modifier = Modifier,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/pinch.json").decodeToString()
        )
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever,
        speed = speed
    )

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress }
        ),
        contentDescription = "Pinch gesture animation",
        modifier = modifier.size(200.dp)
    )
}