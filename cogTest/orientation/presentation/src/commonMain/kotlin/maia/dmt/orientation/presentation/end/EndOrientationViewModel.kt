package maia.dmt.orientation.presentation.end

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.EvaluationDetailString
import maia.dmt.core.domain.dto.evaluation.EvaluationResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.file.ImagePathParams
import maia.dmt.core.domain.usecase.UploadImageUseCase
import maia.dmt.core.domain.util.Result
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.core.presentation.util.toByteArray
import maia.dmt.orientation.presentation.session.OrientationSessionManager

class EndOrientationViewModel(
    private val sessionManager: OrientationSessionManager,
    private val sessionStorage: SessionStorage,
    private val uploadImageUseCase: UploadImageUseCase,
    private val evaluationService: EvaluationService
) : ViewModel() {

    private val _state = MutableStateFlow(EndOrientationState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = EndOrientationState()
    )

    private val eventChannel = Channel<EndOrientationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        uploadAndSubmitResults()
    }

    fun onAction(action: EndOrientationAction) {
        when (action) {
            is EndOrientationAction.OnExitClick -> navigateToHome()
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            eventChannel.send(EndOrientationEvent.NavigateToHome)
        }
    }

    private fun uploadAndSubmitResults() {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val evaluation = sessionManager.evaluation.value
            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo?.user?.id
            val evaluationId = evaluation?.id

            if (clinicId == null || patientId == null || evaluationId == null || evaluation == null) {
                handleError("Missing Session Data")
                return@launch
            }

            val dynamicIds = mutableMapOf<String, Int>()
            evaluation.evaluation_objects.forEach { obj ->
                dynamicIds[obj.object_label] = obj.id
            }

            val currentDateTime = getCurrentFormattedDateTime()
            val accumulatedResults = ArrayList<EvaluationDetailString>()

            // Helper function to upload images
            suspend fun uploadAndAddResult(
                bitmapSource: Any?,
                labelKey: String,
                defaultId: Int,
                fileNamePrefix: String,
                extraData: String
            ) {
                if (bitmapSource == null) {
                    println("DEBUG: Bitmap for $labelKey is null. Skipping.")
                    return
                }

                val targetId = dynamicIds[labelKey] ?: defaultId
                val imageBytes = try {
                    (bitmapSource as androidx.compose.ui.graphics.ImageBitmap).toByteArray()
                } catch (e: Exception) {
                    println("DEBUG: Failed to encode bitmap for $labelKey: ${e.message}")
                    return
                }

                val params = ImagePathParams(
                    clinicId = clinicId,
                    patientId = patientId,
                    evaluationId = evaluationId.toString(),
                    pathDate = currentDateTime,
                    fileName = "${fileNamePrefix}_${targetId}.png",
                    extraData = extraData
                )

                when (val result = uploadImageUseCase.execute(imageBytes, params)) {
                    is Result.Success -> {
                        accumulatedResults.add(
                            EvaluationDetailString(currentDateTime, targetId, result.data)
                        )
                        println("DEBUG: Uploaded $labelKey successfully: ${result.data}")
                    }
                    is Result.Failure -> {
                        println("DEBUG: Upload Failed for $labelKey: ${result.error}")
                    }
                }
            }

            sessionManager.numberSelectionResult.value?.let { result ->
                val selectedNumberId = dynamicIds["selected_number"] ?: 171
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        selectedNumberId,
                        result.selectedNumber?.toString() ?: "No selection"
                    )
                )
                println("DEBUG: Number Selection - Selected: ${result.selectedNumber}, Success: ${result.success}")
            }

            sessionManager.seasonsSelectionResult.value?.let { result ->
                val seasonsId = dynamicIds["selected_seasons"] ?: 172
                val seasonsArray = listOf(
                    result.firstSelection.name,
                    result.secondSelection.name
                )
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        seasonsId,
                        seasonsArray.joinToString(",")
                    )
                )
                println("DEBUG: Seasons Selection - First: ${result.firstSelection}, Second: ${result.secondSelection}")
            }

            sessionManager.dragShapeResult.value?.let { result ->
                val isDraggedId = dynamicIds["is_triangle_dragged"] ?: 173
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        isDraggedId,
                        result.success.toString()
                    )
                )
            }

            sessionManager.shapeResizeResult.value?.let { result ->
                val isSizeChangedId = dynamicIds["is_triangle_size_changed"] ?: 174
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        isSizeChangedId,
                        result.hasResized.toString()
                    )
                )
            }

            sessionManager.drawOrientationResult.value?.let { result ->
                uploadAndAddResult(
                    result.drawingBitmap,
                    "image_url",
                    206,
                    "orientation_drawing",
                    "x_drawing"
                )

                val xDrawingId = dynamicIds["x_drawing"] ?: 175
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        xDrawingId,
                        if (result.hasDrawn) "true" else "false"
                    )
                )
            }

            sessionManager.painScaleResult.value?.let { result ->
                val painLevelId = dynamicIds["health_level"] ?: 176
                accumulatedResults.add(
                    EvaluationDetailString(
                        currentDateTime,
                        painLevelId,
                        result.painLevel.toString()
                    )
                )
            }

            val finalEvaluationResult = EvaluationResult(
                clinicId = clinicId,
                date = currentDateTime,
                evaluation = evaluationId,
                patientId = patientId,
                results = accumulatedResults
            )

            if (accumulatedResults.isNotEmpty()) {
                when (val result = evaluationService.uploadEvaluationResults(finalEvaluationResult)) {
                    is Result.Success -> {
                        println("DEBUG: Upload SUCCESS!")
                        sessionManager.reset()
                        _state.update { it.copy(isUploading = false, uploadSuccess = true) }
                        eventChannel.send(EndOrientationEvent.ShowSuccess("Results uploaded successfully"))
                    }
                    is Result.Failure -> {
                        println("DEBUG: Upload FAILED: ${result.error}")
                        handleError("Failed to submit: ${result.error}")
                    }
                }
            } else {
                handleError("No data gathered to submit.")
            }
        }
    }

    private suspend fun handleError(message: String) {
        println("DEBUG ERROR: $message")
        _state.update { it.copy(isUploading = false, error = message) }
        eventChannel.send(EndOrientationEvent.ShowError(message))
    }
}