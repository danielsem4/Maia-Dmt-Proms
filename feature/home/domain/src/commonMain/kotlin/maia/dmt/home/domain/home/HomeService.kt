package maia.dmt.home.domain.home

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.home.domain.models.Module

interface HomeService {

    suspend fun getModules(clinicId: Int): Result<List<Module>, DataError.Remote>

//    suspend fun getMessages()
}