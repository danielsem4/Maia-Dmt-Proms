package maia.dmt.core.domain.util

import java.util.Locale

actual fun getDeviceLanguageCode(): String {
    return Locale.getDefault().language
}