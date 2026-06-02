package maia.dmt.core.domain.localization

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult

interface LanguageService {
    suspend fun uploadLanguage(languageCode: String): EmptyResult<DataError.Remote>
}
