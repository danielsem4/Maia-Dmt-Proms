package maia.dmt.core.domain.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getDeviceLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}