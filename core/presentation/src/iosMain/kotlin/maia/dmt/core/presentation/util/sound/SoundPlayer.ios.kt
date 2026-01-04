package maia.dmt.core.presentation.util.sound

import platform.AVFoundation.*
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.darwin.NSObject

class IosSoundPlayer(
    private val onCompletion: () -> Unit
) : SoundPlayer {

    private var avPlayer: AVPlayer? = null
    private var observer: NSObject? = null

    override fun play(url: String) {
        stop()

        val nsUrl = NSURL.URLWithString(url) ?: return
        val playerItem = AVPlayerItem(uRL = nsUrl)
        avPlayer = AVPlayer(playerItem = playerItem)

        observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = playerItem,
            queue = null
        ) { _ ->
            onCompletion()
        } as NSObject?

        avPlayer?.play()
    }

    override fun stop() {
        avPlayer?.pause()
        avPlayer = null

        // Remove observer
        observer?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
        }
        observer = null
    }

    override fun release() {
        stop()
    }
}

actual fun createSoundPlayer(onCompletion: () -> Unit): SoundPlayer {
    return IosSoundPlayer(onCompletion)
}