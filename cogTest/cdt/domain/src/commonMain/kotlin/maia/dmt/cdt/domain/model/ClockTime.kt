package maia.dmt.cdt.domain.model

import kotlin.math.PI
import kotlin.math.roundToInt

data class ClockTime(
    val hours: Int,
    val minutes: Int
) {
    init {
        require(hours in 1..12) { "Hours must be between 1 and 12" }
        require(minutes in 0..59) { "Minutes must be between 0 and 59" }
    }

    companion object {
        fun fromAngles(hourAngle: Float, minuteAngle: Float): ClockTime {
            val normalizedHour = ((hourAngle + (PI.toFloat() / 2) + 2 * PI.toFloat()) % (2 * PI.toFloat()))
            val computedHours = (normalizedHour / (2 * PI.toFloat()) * 12f).roundToInt()

            val normalizedMinute = ((minuteAngle + (PI.toFloat() / 2) + 2 * PI.toFloat()) % (2 * PI.toFloat()))
            val computedMinutes = (normalizedMinute / (2 * PI.toFloat()) * 60f).roundToInt()

            val finalHours = if (computedHours == 0) 12 else computedHours
            val finalMinutes = if (computedMinutes == 60) 0 else computedMinutes

            return ClockTime(finalHours, finalMinutes)
        }

        fun toAngles(hours: Int, minutes: Int): Pair<Float, Float> {
            val hoursForAngle = if (hours % 12 == 0) 12f else (hours % 12).toFloat()
            val hourAngle = (hoursForAngle / 12f) * (2 * PI.toFloat()) - (PI.toFloat() / 2)
            val minuteAngle = (minutes / 60f) * (2 * PI.toFloat()) - (PI.toFloat() / 2)
            return Pair(hourAngle, minuteAngle)
        }
    }
}