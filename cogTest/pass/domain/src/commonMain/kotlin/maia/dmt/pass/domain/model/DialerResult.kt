package maia.dmt.pass.domain.model

data class DialerResult(
    val dialerOpenResult: DialerOpenResult,
    val dialToDentistPhaseOneResult: DialToDentistPhaseOneResult,
    val dialToDentistPhaseTwoResult: DialToDentistPhaseTwoResult,
)

data class DialerOpenResult(
    val inactivityCount: Int = 0,
)

data class DialToDentistPhaseOneResult(
    val inactivityCount: Int = 0,
    val wrongNumberDialedCount: Int = 0,
    val numbersDialed: List<String> = emptyList(),
)

data class DialToDentistPhaseTwoResult(
    val inactivityCount: Int = 0,
    val wrongNumberDialedCount: Int = 0,
    val numbersDialed: List<String> = emptyList(),
)
