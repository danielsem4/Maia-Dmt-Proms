package maia.dmt.cdt.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import maia.dmt.core.designsystem.components.capture.CapturableBox
import maia.dmt.core.presentation.capture.ViewCaptureController

import maia.dmt.core.presentation.capture.rememberViewCaptureController

@Composable
fun CapturableInteractiveClock(
    hourAngle: Float,
    minuteAngle: Float,
    onHourAngleChange: (Float) -> Unit,
    onMinuteAngleChange: (Float) -> Unit,
    captureController: ViewCaptureController,
    modifier: Modifier = Modifier,
    config: InteractiveClockConfig = InteractiveClockConfig(
        clockColor = MaterialTheme.colorScheme.primary,
        hourHandColor = MaterialTheme.colorScheme.primary,
        minuteHandColor = MaterialTheme.colorScheme.primary,
        numberColor = MaterialTheme.colorScheme.primary,
        dotColor = MaterialTheme.colorScheme.primary
    ),
    enabled: Boolean = true
) {
    CapturableBox(
        controller = captureController,
        modifier = modifier.aspectRatio(1f)
    ) {
        Box(modifier = Modifier.matchParentSize().background(Color.White)) {
            InteractiveClock(
                hourAngle = hourAngle,
                minuteAngle = minuteAngle,
                onHourAngleChange = onHourAngleChange,
                onMinuteAngleChange = onMinuteAngleChange,
                modifier = Modifier.matchParentSize(),
                config = config,
                enabled = enabled
            )
        }
    }
}

@Composable
fun rememberClockCaptureController(): ViewCaptureController = rememberViewCaptureController()