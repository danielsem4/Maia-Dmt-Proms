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
        0 to listOf("02:30", "11:40"),
        1 to listOf("03:20", "01:40"),
        2 to listOf("01:30", "12:40"),
        3 to listOf("10:10", "12:50"),
        4 to listOf("12:40", "01:05")
    )

    fun onAction(action: CdtEndAction) {
        when (action) {
            is CdtEndAction.OnExitClick -> uploadAndSubmitResults()
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

            // Helper function (Same as before)
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
                        println("DEBUG: Upload Success for $labelKey. Path: ${result.data}")
                        accumulatedResults.add(
                            MeasurementDetailString(
                                dateTime = currentDateTime,
                                measureObject = targetId,
                                value = result.data
                            )
                        )
                    }
                    is Result.Failure -> {
                        println("DEBUG: Upload Failed for $labelKey: ${result.error}")
                    }
                }
            }

            // ---------------------------------------------------------
            // 1. FIX: Find the Main Drawing by LABEL, not by Index 0
            // ---------------------------------------------------------
            val drawings = cdtSessionManager.getAllDrawingBitmaps()

            // Find which index in the list corresponds to "imageUrl"
            val mainDrawingIndex = evaluation.measurement_objects.indexOfFirst {
                it.object_label == "imageUrl"
            }

            // If found, get that specific index. If not found, try 0 as a fallback.
            val mainDrawingBitmap = if (mainDrawingIndex != -1) {
                drawings[mainDrawingIndex]
            } else {
                drawings[0]
            }

            // Debug log to see what's happening
            println("DEBUG: Main Drawing Index: $mainDrawingIndex, Available Keys: ${drawings.keys}")

            uploadAndAddResult(
                bitmapSource = mainDrawingBitmap,
                labelKey = "imageUrl",
                defaultId = 186,
                fileNamePrefix = "cdt_main",
                extraData = "main_drawing"
            )

            // ---------------------------------------------------------
            // 2. Upload Clock Set Bitmaps
            // Note: These usually work with 0 and 1 because the ClockTimeViewModel
            // has its own internal index starting at 0.
            // ---------------------------------------------------------
            val clockBitmaps = cdtSessionManager.getAllClockBitmaps()

            uploadAndAddResult(
                bitmapSource = clockBitmaps[0],
                labelKey = "Time1 imageUrl",
                defaultId = 585,
                fileNamePrefix = "cdt_time1",
                extraData = "time1_image"
            )

            uploadAndAddResult(
                bitmapSource = clockBitmaps[1],
                labelKey = "Time2 imageUrl",
                defaultId = 586,
                fileNamePrefix = "cdt_time2",
                extraData = "time2_image"
            )

            // ---------------------------------------------------------
            // 3. Text Results
            // ---------------------------------------------------------
            val userTimes = cdtSessionManager.getAllClockTimes()
            val examVersion = cdtSessionManager.getClockExamVersion()
            val requestedTimeStrings = requestedTimesByVersion[examVersion] ?: listOf("00:00", "00:00")

            if (userTimes.containsKey(0)) {
                val userTime = userTimes[0]!!
                val actualId = dynamicIds["Actual time 1"] ?: 193
                val userTimeString = "${formatTime(userTime.hours)}:${formatTime(userTime.minutes)}"
                accumulatedResults.add(MeasurementDetailString(currentDateTime, actualId, userTimeString))

                val requestedId = dynamicIds["Requested time 1"] ?: 190
                val expectedString = requestedTimeStrings.getOrElse(0) { "00:00" }
                accumulatedResults.add(MeasurementDetailString(currentDateTime, requestedId, expectedString))
            }

            if (userTimes.containsKey(1)) {
                val userTime = userTimes[1]!!
                val actualId = dynamicIds["Actual time 2"] ?: 194
                val userTimeString = "${formatTime(userTime.hours)}:${formatTime(userTime.minutes)}"
                accumulatedResults.add(MeasurementDetailString(currentDateTime, actualId, userTimeString))

                val requestedId = dynamicIds["Requested time 2"] ?: 191
                val expectedString = requestedTimeStrings.getOrElse(1) { "00:00" }
                accumulatedResults.add(MeasurementDetailString(currentDateTime, requestedId, expectedString))
            }

            // Submit
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
                    is Result.Failure -> {
                        handleError("Failed to submit results: ${result.error}")
                    }
                }
            } else {
                handleError("No data gathered to submit.")
            }
        }
    }

    private fun formatTime(value: Int): String {
        return value.toString().padStart(2, '0')
    }

    private suspend fun handleError(message: String) {
        _state.update { it.copy(isUploading = false, error = message) }
        eventChannel.send(CdtEndEvent.ShowError(message))
    }
}