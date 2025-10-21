package maia.dmt.evaluation.presentation.evaluation

import maia.dmt.core.presentation.util.UiText

interface EvaluationEvent {

    data object NavigateBack: EvaluationEvent
    data class UploadSuccess(val message: UiText) : EvaluationEvent
    data class UploadError(val error: UiText) : EvaluationEvent

}