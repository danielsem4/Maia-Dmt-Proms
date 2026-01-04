package maia.dmt.core.presentation.util.sound

import android.media.AudioAttributes
import android.media.MediaPlayer

class AndroidSoundPlayer(
    private val onCompletion: () -> Unit
) : SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(url: String) {
        stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnCompletionListener { onCompletion() }
                setOnPreparedListener { start() }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onCompletion()
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    override fun release() {
        stop()
    }
}

actual fun createSoundPlayer(onCompletion: () -> Unit): SoundPlayer {
    return AndroidSoundPlayer(onCompletion)
}