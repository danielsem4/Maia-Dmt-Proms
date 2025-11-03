package maia.dmt.settings.presentation.language

import maia.dmt.core.domain.localization.Language

data class LanguageState(
    val currentLanguage: Language = Language.English,
    val newSelection: Language? = null,
    val isLoading: Boolean = false,
)