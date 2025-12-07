package maia.dmt.cdt.domain.usecase

import maia.dmt.cdt.domain.model.ClockExam
import maia.dmt.cdt.domain.model.ClockMission
import maia.dmt.cdt.domain.model.ClockTime
import kotlin.random.Random

class GetRandomClockExamUseCase {

    operator fun invoke(missions: Map<Int, List<String>>): ClockExam {
        val versions = missions.keys.toList()
        val selectedVersion = versions[Random.nextInt(versions.size)]
        val missionTexts = missions[selectedVersion] ?: emptyList()

        val parsedMissions = missionTexts.mapIndexed { index, text ->
            val expectedTime = if (index == 0) {
                calculateExpectedTime(12, 0, text)
            } else {

                val previousTime = calculateExpectedTime(12, 0, missionTexts[0])
                calculateExpectedTime(previousTime.hours, previousTime.minutes, text)
            }
            ClockMission(instruction = text, expectedTime = expectedTime)
        }

        return ClockExam(version = selectedVersion, missions = parsedMissions)
    }

    private fun calculateExpectedTime(startHour: Int, startMinute: Int, instruction: String): ClockTime {
        val hoursMatch = Regex("""(\d+)\s+hour""").find(instruction)
        val minutesMatch = Regex("""(\d+)\s+minute""").find(instruction)

        val hours = hoursMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val minutes = minutesMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0

        val isForward = instruction.contains("forward", ignoreCase = true)
        val isBackward = instruction.contains("backward", ignoreCase = true)

        var totalMinutes = startHour * 60 + startMinute
        val deltaMinutes = hours * 60 + minutes

        totalMinutes = when {
            isForward -> totalMinutes + deltaMinutes
            isBackward -> totalMinutes - deltaMinutes
            else -> totalMinutes
        }

        // Normalize to 12-hour format
        totalMinutes = ((totalMinutes % (12 * 60)) + (12 * 60)) % (12 * 60)

        val resultHour = (totalMinutes / 60).let { if (it == 0) 12 else it }
        val resultMinute = totalMinutes % 60

        return ClockTime(hours = resultHour, minutes = resultMinute)
    }
}