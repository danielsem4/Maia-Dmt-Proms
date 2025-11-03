package maia.dmt.core.domain.localization

import platform.Foundation.NSUserDefaults

actual class Localization {
    actual fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }
}