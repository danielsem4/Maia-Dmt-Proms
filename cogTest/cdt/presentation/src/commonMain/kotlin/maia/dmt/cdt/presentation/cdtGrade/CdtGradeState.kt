package maia.dmt.cdt.presentation.cdtGrade

import androidx.compose.ui.graphics.ImageBitmap

data class CdtGradeState(
    val clockBitmap: ImageBitmap? = null,
    val selectedCircleGrade: String? = null,
    val selectedNumbersGrade: String? = null,
    val selectedHandsGrade: String? = null,
    val circleOptions: List<String> = emptyList(),
    val numbersOptions: List<String> = emptyList(),
    val handsOptions: List<String> = emptyList()
)
