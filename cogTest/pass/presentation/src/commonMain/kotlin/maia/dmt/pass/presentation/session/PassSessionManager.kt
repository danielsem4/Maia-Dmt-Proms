package maia.dmt.pass.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.pass.domain.model.ApplicationsScreenResult
import maia.dmt.pass.domain.model.ContactScreenResult
import maia.dmt.pass.domain.model.ContactsScreenResult
import maia.dmt.pass.domain.model.DialToDentistPhaseOneResult
import maia.dmt.pass.domain.model.DialToDentistPhaseTwoResult
import maia.dmt.pass.domain.model.DialerOpenResult
import maia.dmt.pass.domain.model.DialerResult

class PassSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) { _evaluation.update { evaluation } }
    fun setLoading(isLoading: Boolean) { _isLoading.update { isLoading } }
    fun setError(error: String?) { _error.update { error } }

    private val _applicationsScreenResult = MutableStateFlow(ApplicationsScreenResult())
    val applicationsScreenResult: StateFlow<ApplicationsScreenResult> = _applicationsScreenResult.asStateFlow()

    fun saveApplicationsScreenResult(
        inactivityCount: Int,
        wrongAppPressCount: Int,
        appsPressed: List<String>
    ) {
        _applicationsScreenResult.update {
            ApplicationsScreenResult(
                inactivityCount = inactivityCount,
                wrongAppPressCount = wrongAppPressCount,
                appsPressed = appsPressed
            )
        }
    }

    private var _appsScreenSnapshot = ApplicationsScreenResult()

    fun saveAppsSnapshot(result: ApplicationsScreenResult) {
        _appsScreenSnapshot = result
    }

    fun getAppsSnapshot(): ApplicationsScreenResult {
        return _appsScreenSnapshot
    }

    private val _contactsScreenResult = MutableStateFlow(ContactsScreenResult())
    val contactsScreenResult: StateFlow<ContactsScreenResult> = _contactsScreenResult.asStateFlow()

    fun saveContactsScreenResult(
        inactivityCount: Int,
        wrongAppPressCount: Int,
        contactsPressed: List<String>
    ) {
        _contactsScreenResult.update {
            ContactsScreenResult(
                inactivityCount = inactivityCount,
                wrongAppPressCount = wrongAppPressCount,
                contactsPressed = contactsPressed
            )
        }
    }

    private val _contactScreenResult = MutableStateFlow(ContactScreenResult())
    val contactScreenResult: StateFlow<ContactScreenResult> = _contactScreenResult.asStateFlow()

    fun saveContactScreenResult(
        inactivityCount: Int,
        wrongAppPressCount: Int,
        buttonsPressed: List<String>
    ) {
        _contactScreenResult.update {
            ContactScreenResult(
                inactivityCount = inactivityCount,
                wrongAppPressCount = wrongAppPressCount,
                buttonsPressed = buttonsPressed
            )
        }
    }

    private val _dialerResult = MutableStateFlow(
        DialerResult(
            dialerOpenResult = DialerOpenResult(),
            dialToDentistPhaseOneResult = DialToDentistPhaseOneResult(),
            dialToDentistPhaseTwoResult = DialToDentistPhaseTwoResult()
        )
    )
    val dialerResult: StateFlow<DialerResult> = _dialerResult.asStateFlow()

    fun saveDialerOpenResult(inactivityCount: Int) {
        _dialerResult.update { current ->
            current.copy(
                dialerOpenResult = DialerOpenResult(inactivityCount = inactivityCount)
            )
        }
    }

    fun saveDialToDentistPhaseOne(
        inactivityCount: Int,
        wrongNumberDialedCount: Int,
        numbersDialed: List<String>
    ) {
        _dialerResult.update { current ->
            current.copy(
                dialToDentistPhaseOneResult = DialToDentistPhaseOneResult(
                    inactivityCount = inactivityCount,
                    wrongNumberDialedCount = wrongNumberDialedCount,
                    numbersDialed = numbersDialed
                )
            )
        }
    }

    fun saveDialToDentistPhaseTwo(
        inactivityCount: Int,
        wrongNumberDialedCount: Int,
        numbersDialed: List<String>
    ) {
        _dialerResult.update { current ->
            current.copy(
                dialToDentistPhaseTwoResult = DialToDentistPhaseTwoResult(
                    inactivityCount = inactivityCount,
                    wrongNumberDialedCount = wrongNumberDialedCount,
                    numbersDialed = numbersDialed
                )
            )
        }
    }

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _applicationsScreenResult.value = ApplicationsScreenResult()
        _contactsScreenResult.value = ContactsScreenResult()
        _contactScreenResult.value = ContactScreenResult()
        _dialerResult.value = DialerResult(
            DialerOpenResult(),
            DialToDentistPhaseOneResult(),
            DialToDentistPhaseTwoResult()
        )
    }
}