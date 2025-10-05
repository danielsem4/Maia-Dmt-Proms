package maia.dmt.core.domain.auth

import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result

interface AuthService {

    suspend fun login(
        email: String,
        password: String
    ): Result<LoginSuccessfulRequest, DataError.Remote>

}