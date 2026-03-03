package maia.dmt.core.presentation.util.sound

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFoundation.*
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
class IosSoundPlayer(
    private val onCompletion: () -> Unit
) : SoundPlayer {

    private var avPlayer: AVPlayer? = null
    private var observerToken: Any? = null

    override fun play(url: String) {
        stop()

        // Activate audio session so playback works regardless of silent switch
        AVAudioSession.sharedInstance()
            .setCategory(AVAudioSessionCategoryPlayback, error = null)

        val nsUrl = NSURL.URLWithString(url) ?: run {
            onCompletion()
            return
        }
        val playerItem = AVPlayerItem(uRL = nsUrl)
        avPlayer = AVPlayer(playerItem = playerItem)

        // Use mainQueue so the completion callback arrives on the main thread
        observerToken = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            onCompletion()
        }

        avPlayer?.play()
    }

    override fun stop() {
        avPlayer?.pause()
        avPlayer = null

        observerToken?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
        }
        observerToken = null
    }

    override fun release() {
        stop()
    }
}

actual fun createSoundPlayer(onCompletion: () -> Unit): SoundPlayer {
    return IosSoundPlayer(onCompletion)
}