package maia.dmt.market.domain.service

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.market.domain.model.MarketProduct

interface MarketService {

    suspend fun getProducts(
        clinicId: Int,
        patientId: Int
    ): Result<List<MarketProduct>, DataError.Remote>
}