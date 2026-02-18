package maia.dmt.hitber.presentation.hitberFirstQuestion

import maia.dmt.core.domain.dto.evaluation.EvaluationObject

data class HitberFirstQuestionState(
    val questions: List<EvaluationObject> = emptyList(),
    val currentBatchIndex: Int = 0,
    val answers: Map<Int, String> = emptyMap(),
    val hasAttemptedCurrentBatch: Boolean = false,
)
