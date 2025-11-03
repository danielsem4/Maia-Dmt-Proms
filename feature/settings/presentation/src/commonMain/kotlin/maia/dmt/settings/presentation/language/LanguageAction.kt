package maia.dmt.settings.presentation.language

import maia.dmt.core.domain.localization.Language

sealed interface LanguageAction {
    data object OnBackClick : LanguageAction
    data class OnLanguageSelect(val language: Language) : LanguageAction
    data object OnSaveClick : LanguageAction
}