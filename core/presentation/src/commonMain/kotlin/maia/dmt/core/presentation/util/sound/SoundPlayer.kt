package maia.dmt.core.presentation.util.sound

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

interface SoundPlayer {
    fun play(url: String)
    fun stop()
    fun release()
}

@Composable
fun rememberSoundPlayer(onCompletion: () -> Unit): SoundPlayer {
    val player = remember { createSoundPlayer(onCompletion) }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    return player
}

expect fun createSoundPlayer(onCompletion: () -> Unit): SoundPlayer