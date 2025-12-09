package maia.dmt.cdt.presentation.cdtClockTimeSet

import maia.dmt.cdt.domain.model.ClockTime

data class CdtClockTimeSetState(
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 2,
    val hourHandAngle: Float = 11f,
    val minuteHandAngle: Float = 11f,
    val instructionText: String = "",
    val isLoading: Boolean = false,
    val selectedExamVersion: Int = 1
) {
    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= totalQuestions - 1

    fun getCurrentTime(): ClockTime = ClockTime.fromAngles(hourHandAngle, minuteHandAngle)
}