package maia.dmt.pass.presentation.passApps

data class PassApplicationsState(

    val showInstructionDialog: Boolean = true,
    val showConfirmationDialog: Boolean = false,
    val isRetryMode: Boolean = false,


    val isTestActive: Boolean = false,

    
    val inactiveCount: Int = 0,
    val wrongAppPressCount: Int = 0,
    val showTimeoutDialog: Boolean = false
)