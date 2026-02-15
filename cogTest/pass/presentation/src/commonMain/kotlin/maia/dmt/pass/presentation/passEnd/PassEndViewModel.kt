package maia.dmt.pass.presentation.passEnd

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
import maia.dmt.core.domain.dto.MeasurementDetailString
import maia.dmt.core.domain.dto.evaluation.MeasurementResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.Result
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.pass.presentation.session.PassSessionManager

class PassEndViewModel(
    private val sessionManager: PassSessionManager,
    private val evaluationService: EvaluationService,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(PassEndState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassEndState()
    )

    private val _events = Channel<PassEndEvent>()
    val events = _events.receiveAsFlow()

    init {
        uploadAndSubmitResults()
    }

    fun onAction(action: PassEndAction) {
        when (action) {
            PassEndAction.OnAudioFinished -> {
                _state.update { it.copy(isPlayingAudio = false) }
            }
            PassEndAction.OnFinishClick -> {
                sessionManager.clear()
                viewModelScope.launch {
                    _events.send(PassEndEvent.NavigateToHome)
                }
            }
        }
    }

    private fun uploadAndSubmitResults() {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val evaluation = sessionManager.evaluation.value
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

            // Screen 1: Applications Screen
            sessionManager.applicationsScreenResult.value.let { result ->
                val transfersId = dynamicIds["Applications screen - Number of transfers"] ?: 353
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersId,
                        result.inactivityCount.toString()
                    )
                )

                val incorrectClicksId = dynamicIds["Applications screen - Number of incorrect clicks"] ?: 354
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        incorrectClicksId,
                        result.wrongAppPressCount.toString()
                    )
                )

                val diagnosisId = dynamicIds["Applications screen - Diagnosis"] ?: 355
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisId,
                        result.appsPressed.joinToString(",")
                    )
                )
            }

            // Screen 2: Application Screen (Two Parts)
            sessionManager.getAppsSnapshot().let { snapshot ->
                val transfersPart1Id = dynamicIds["Application screen - Number of transfers (part 1)"] ?: 356
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersPart1Id,
                        snapshot.inactivityCount.toString()
                    )
                )

                val diagnosisPart1Id = dynamicIds["Application screen - Diagnosis (part 1)"] ?: 357
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisPart1Id,
                        snapshot.appsPressed.joinToString(",")
                    )
                )
            }

            sessionManager.applicationsScreenResult.value.let { result ->
                val transfersPart2Id = dynamicIds["Application screen - Number of transfers (part 2)"] ?: 358
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersPart2Id,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisPart2Id = dynamicIds["Application screen - Diagnosis (part 2)"] ?: 359
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisPart2Id,
                        result.appsPressed.joinToString(",")
                    )
                )
            }

            // Screen 3: Contact List
            sessionManager.contactsScreenResult.value.let { result ->
                val transfersId = dynamicIds["Contact list  - Number of transfers"] ?: 360
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersId,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisId = dynamicIds["Contact list - Diagnosis"] ?: 361
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisId,
                        result.contactsPressed.joinToString(",")
                    )
                )
            }

            // Screen 4: Contact Screen
            sessionManager.contactScreenResult.value.let { result ->
                val transfersId = dynamicIds["Contact screen - Number of transfers"] ?: 362
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersId,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisId = dynamicIds["Contact screen - Diagnosis"] ?: 363
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisId,
                        result.buttonsPressed.joinToString(",")
                    )
                )

                val part1SummaryId = dynamicIds["Part 1 summary"] ?: 364
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        part1SummaryId,
                        "Part 1 completed"
                    )
                )
            }

            // Screen 5: Opening the Dialer
            sessionManager.dialerResult.value.dialerOpenResult.let { result ->
                val transfersId = dynamicIds["Opening the dialer - Number of transfers"] ?: 365
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersId,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisId = dynamicIds["Opening the dialer - Diagnosis"] ?: 366
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisId,
                        "Dialer opened"
                    )
                )
            }

            // Screen 6: Dialing a Dental Clinic (Two Parts)
            sessionManager.dialerResult.value.dialToDentistPhaseOneResult.let { result ->
                val transfersPart1Id = dynamicIds["Dialing a dental clinic - Number of transfers (part 1)"] ?: 367
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersPart1Id,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisPart1Id = dynamicIds["Dialing a dental clinic - Diagnosis (part 1)"] ?: 368
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisPart1Id,
                        result.numbersDialed.joinToString(",")
                    )
                )
            }

            sessionManager.dialerResult.value.dialToDentistPhaseTwoResult.let { result ->
                val transfersPart2Id = dynamicIds["Dialing a dental clinic - Number of transfers (part 2)"] ?: 369
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        transfersPart2Id,
                        result.inactivityCount.toString()
                    )
                )

                val diagnosisPart2Id = dynamicIds["Dialing a dental clinic - Diagnosis (part 2)"] ?: 370
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        diagnosisPart2Id,
                        result.numbersDialed.joinToString(",")
                    )
                )

                val part2SummaryId = dynamicIds["Part 2 summary"] ?: 371
                accumulatedResults.add(
                    MeasurementDetailString(
                        currentDateTime,
                        part2SummaryId,
                        "Part 2 completed"
                    )
                )
            }

            // Submit all results
            val finalMeasurementResult = MeasurementResult(
                clinicId = clinicId,
                date = currentDateTime,
                measurement = evaluationId,
                patientId = patientId,
                results = accumulatedResults
            )

            if (accumulatedResults.isNotEmpty()) {
                when (val result = evaluationService.uploadEvaluationResults(finalMeasurementResult)) {
                    is Result.Success -> {
                        println("DEBUG: Upload SUCCESS!")
                        sessionManager.clear()
                        _state.update { it.copy(isUploading = false, uploadSuccess = true) }
                        _events.send(PassEndEvent.ShowSuccess("Results uploaded successfully"))
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
        _events.send(PassEndEvent.ShowError(message))
    }
}