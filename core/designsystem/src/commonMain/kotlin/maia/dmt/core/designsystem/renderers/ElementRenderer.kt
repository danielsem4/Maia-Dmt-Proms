package maia.dmt.core.designsystem.renderers

import androidx.compose.runtime.Composable
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement

interface ElementRenderer {
    fun canRender(elementType: ElementType): Boolean

    @Composable
    fun Render(
        element: MeasurementElement,
        currentAnswer: String,
        onAnswerChange: (String) -> Unit
    )
}
