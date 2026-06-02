package maia.dmt.core.data.localization

import io.ktor.client.HttpClient
import maia.dmt.core.data.dto.LanguageRequest
import maia.dmt.core.data.networking.patch
import maia.dmt.core.domain.localization.LanguageService
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.asEmptyResult

class KtorLanguageService(
    private val httpClient: HttpClient
) : LanguageService {

    override suspend fun uploadLanguage(
        languageCode: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.patch<LanguageRequest, Unit>(
            route = "api/v1/mobile/language/",
            body = LanguageRequest(language = languageCode)
        ).asEmptyResult()
    }
}
