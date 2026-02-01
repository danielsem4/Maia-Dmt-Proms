package maia.dmt.proms.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.compositionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import maia.dmt.core.domain.sensors.storage.DeletionTracker
import maia.dmt.proms.keyboard.ui.KeyboardMode
import maia.dmt.proms.keyboard.ui.KeyboardScreen
import org.koin.android.ext.android.inject

class SensorCustomKeyboardService : InputMethodService() {

    private val deletionTracker: DeletionTracker by inject()
    private var currentMode by mutableStateOf(KeyboardMode.QWERTY)
    private var coroutineScope: CoroutineScope? = null

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this)

        // Create Recomposer for Compose
        coroutineScope = CoroutineScope(AndroidUiDispatcher.CurrentThread)
        val recomposer = Recomposer(coroutineScope!!.coroutineContext)
        composeView.compositionContext = recomposer

        coroutineScope!!.launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        composeView.setContent {
            KeyboardScreen(
                onKeyPress = { code -> handleKeyPress(code) },
                onDelete = { handleDelete() },
                onSpace = { handleSpace() },
                onDone = { handleDone() },
                onModeChange = { mode -> currentMode = mode },
                currentMode = currentMode
            )
        }

        return composeView
    }

    private fun handleKeyPress(code: Int) {
        currentInputConnection?.commitText(String(Character.toChars(code)), 1)
    }

    private fun handleDelete() {
        val selectedText = currentInputConnection?.getTextBeforeCursor(1, 0)
        if (!selectedText.isNullOrEmpty()) {
            currentInputConnection?.deleteSurroundingText(1, 0)
            deletionTracker.incrementDeleteCount()
        }
    }

    private fun handleSpace() {
        currentInputConnection?.commitText(" ", 1)
    }

    private fun handleDone() {
        currentInputConnection?.sendKeyEvent(
            android.view.KeyEvent(
                android.view.KeyEvent.ACTION_DOWN,
                android.view.KeyEvent.KEYCODE_ENTER
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope?.cancel()
    }
}