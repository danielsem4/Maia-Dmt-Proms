package maia.dmt.core.domain.dto.evaluation

data class EvaluationSettings(
    val id: Int,
    val evaluation_repeat_period: String,
    val evaluation_repeat_times: Double?,
    val evaluation_begin_time: String,
    val evaluation_last_time: String?,
    val evaluation_end_time: String,
    val is_repetitive: Boolean,
    val times_taken: Int,
    val patient: Int,
    val doctor: Int,
    val clinic: Int,
    val evaluation: Int
)
