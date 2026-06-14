package maia.dmt.home.domain.home

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.home.domain.models.HomeData

interface HomeService {

    suspend fun getHomeData(clinicId: String, userId: String): Result<HomeData, DataError.Remote>

    suspend fun getDoctorHomeData(clinicId: String): Result<HomeData, DataError.Remote>

}
