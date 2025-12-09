package maia.dmt.cdt.presentation.cdtEnd

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
import maia.dmt.cdt.presentation.session.CdtSessionManager
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.MeasurementDetailString
import maia.dmt.core.domain.dto.evaluation.MeasurementResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.file.ImagePathParams
import maia.dmt.core.domain.usecase.UploadImageUseCase
import maia.dmt.core.domain.util.Result
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.core.presentation.util.toByteArray

class CdtEndViewModel(
    private val cdtSessionManager: CdtSessionManager,
    private val sessionStorage: SessionStorage,
    private val uploadImageUseCase: UploadImageUseCase,
    private val evaluationService: EvaluationService
) : ViewModel() {

    private val _state = MutableStateFlow(CdtEndState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = CdtEndState()
    )

    private val eventChannel = Channel<CdtEndEvent>()
    val events = eventChannel.receiveAsFlow()

    private val requestedTimesByVersion: Map<Int, List<String>> = mapOf(
        0 to listOf("02:30", "11:10"),
        1 to listOf("03:20", "10:20"),
        2 to listOf("01:30", "11:10"),
        3 to listOf("10:10", "02:40"),
        4 to listOf("12:40", "01:25")
    )

    fun onAction(action: CdtEndAction) {
        when (action) {
            is CdtEndAction.OnExitClick -> uploadAndSubmitResults()
            is CdtEndAction.OnGradeClick -> navigateToGrade()
        }
    }

    private fun navigateToGrade() {
        viewModelScope.launch {
            eventChannel.send(CdtEndEvent.NavigateToGrade)
        }
    }

    private fun uploadAndSubmitResults() {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val evaluation = cdtSessionManager.evaluation.value
            val clinicId = authInfo?.user?.clinicId
            val patientId = authInfo?.user?.id
            val evaluationId = evaluation?.id

            if (clinicId == null || patientId == null || evaluationId == null || evaluation == null) {
                handleError("Missing Session Data")
                return@launch
            }

            val dynamicIds = mutableMapOf<String, Int>()
            evaluation.measurement_objects.forEach { obj ->
                dynamicIds[obj.object_label] = obj.id
            }

            val currentDateTime = getCurrentFormattedDateTime()
            val accumulatedResults = ArrayList<MeasurementDetailString>()

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
                    measurementId = evaluationId.toString(),
                    pathDate = currentDateTime,
                    fileName = "${fileNamePrefix}_${targetId}.png",
                    extraData = extraData
                )

                when (val result = uploadImageUseCase.execute(imageBytes, params)) {
                    is Result.Success -> {
                        accumulatedResults.add(MeasurementDetailString(currentDateTime, targetId, result.data))
                    }
                    is Result.Failure -> {
                        println("DEBUG: Upload Failed for $labelKey: ${result.error}")
                    }
                }
            }

            val drawings = cdtSessionManager.getAllDrawingBitmaps()
            val mainDrawingIndex = evaluation.measurement_objects.indexOfFirst { it.object_label == "imageUrl" }
            val mainDrawingBitmap = if (mainDrawingIndex != -1 && drawings.containsKey(mainDrawingIndex)) {
                drawings[mainDrawingIndex]
            } else {
                drawings[0]
            }
            uploadAndAddResult(mainDrawingBitmap, "imageUrl", 186, "cdt_main", "main_drawing")

            val clockBitmaps = cdtSessionManager.getAllClockBitmaps()
            uploadAndAddResult(clockBitmaps[0], "Time1 imageUrl", 585, "cdt_time1", "time1_image")
            uploadAndAddResult(clockBitmaps[1], "Time2 imageUrl", 586, "cdt_time2", "time2_image")

            val userTimes = cdtSessionManager.getAllClockTimes()
            val examVersion = cdtSessionManager.getClockExamVersion()
            val requestedTimeStrings = requestedTimesByVersion[examVersion] ?: listOf("00:00", "00:00")

            if (userTimes.containsKey(0)) {
                val userTime = userTimes[0]!!
                val actualId = dynamicIds["Actual time 1"] ?: 193
                accumulatedResults.add(MeasurementDetailString(currentDateTime, actualId, "${formatTime(userTime.hours)}:${formatTime(userTime.minutes)}"))

                val requestedId = dynamicIds["Requested time 1"] ?: 190
                accumulatedResults.add(MeasurementDetailString(currentDateTime, requestedId, requestedTimeStrings.getOrElse(0) { "00:00" }))
            }

            if (userTimes.containsKey(1)) {
                val userTime = userTimes[1]!!
                val actualId = dynamicIds["Actual time 2"] ?: 194
                accumulatedResults.add(MeasurementDetailString(currentDateTime, actualId, "${formatTime(userTime.hours)}:${formatTime(userTime.minutes)}"))

                val requestedId = dynamicIds["Requested time 2"] ?: 191
                accumulatedResults.add(MeasurementDetailString(currentDateTime, requestedId, requestedTimeStrings.getOrElse(1) { "00:00" }))
            }

            val grades = cdtSessionManager.grades.value

            if (grades.circle.isNotEmpty()) {
                val id = dynamicIds["circle_perfection"] ?: 89
                accumulatedResults.add(MeasurementDetailString(currentDateTime, id, grades.circle))
            }

            if (grades.numbers.isNotEmpty()) {
                val id = dynamicIds["numbers_sequence"] ?: 90
                accumulatedResults.add(MeasurementDetailString(currentDateTime, id, grades.numbers))
            }

            if (grades.hands.isNotEmpty()) {
                val id = dynamicIds["hands_position"] ?: 91
                accumulatedResults.add(MeasurementDetailString(currentDateTime, id, grades.hands))
            }

            if (accumulatedResults.isNotEmpty()) {
                val finalMeasurementResult = MeasurementResult(
                    clinicId = clinicId,
                    date = currentDateTime,
                    measurement = evaluationId,
                    patientId = patientId,
                    results = accumulatedResults
                )

                when (val result = evaluationService.uploadEvaluationResults(finalMeasurementResult)) {
                    is Result.Success -> {
                        cdtSessionManager.clear()
                        _state.update { it.copy(isUploading = false) }
                        eventChannel.send(CdtEndEvent.NavigateToHome)
                    }
                    is Result.Failure -> handleError("Failed to submit: ${result.error}")
                }
            } else {
                handleError("No data gathered to submit.")
            }
        }
    }

    private fun formatTime(value: Int) = value.toString().padStart(2, '0')
    private suspend fun handleError(message: String) {
        _state.update { it.copy(isUploading = false, error = message) }
        eventChannel.send(CdtEndEvent.ShowError(message))
    }
}