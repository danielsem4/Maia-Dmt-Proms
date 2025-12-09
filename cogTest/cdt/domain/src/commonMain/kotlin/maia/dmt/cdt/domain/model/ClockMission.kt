package maia.dmt.cdt.domain.model

data class ClockMission(
    val instruction: String,
    val expectedTime: ClockTime
)

data class ClockExam(
    val version: Int,
    val missions: List<ClockMission>
)