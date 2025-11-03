package maia.dmt.core.domain.localization

import android.content.Context
import android.os.LocaleList
import java.util.Locale
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual class Localization(
    private val context: Context
) {
    actual fun applyLanguage(iso: String) {
        val locale = Locale(iso)
        Locale.setDefault(locale)

        // Update app configuration
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        config.setLocales(LocaleListCompat.forLanguageTags(iso).unwrap() as LocaleList)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Use AppCompat's locale API for better persistence
        val appLocale = LocaleListCompat.forLanguageTags(iso)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}