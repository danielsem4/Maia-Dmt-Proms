package maia.dmt.market.data.service

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.market.data.mapper.toDomain
import maia.dmt.market.data.model.MarketResponseContainer
import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.service.MarketService

class KtorMarketService(
    private val httpClient: HttpClient
) : MarketService {

    override suspend fun getProducts(
        clinicId: Int,
        patientId: Int
    ): Result<List<MarketProduct>, DataError.Remote> {
        return httpClient.get<MarketResponseContainer>(
            route = "loadMeasurementJson/$clinicId/75/"
        ).map { response ->
            response.data.products.toDomain()
        }
    }
}