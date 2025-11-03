package maia.dmt.core.domain.localization

enum class Language(val iso: String, val displayName: String) {
    English(iso = "en", displayName = "English"),
    Hebrew(iso = "he", displayName = "עברית"),
    Russian(iso = "ru", displayName = "Русский"),
    Arabic(iso = "ar", displayName = "العربية");

    companion object {
        fun fromIso(iso: String): Language? {
            return entries.find { it.iso == iso }
        }

        fun fromDisplayName(displayName: String): Language? {
            return entries.find { it.displayName == displayName }
        }
    }
}