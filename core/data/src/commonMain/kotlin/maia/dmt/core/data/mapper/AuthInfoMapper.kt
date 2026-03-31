package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.LoginSuccessfulRequestSerializable
import maia.dmt.core.data.dto.ModuleSerializable
import maia.dmt.core.data.dto.UserSerializable
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.dto.Module
import maia.dmt.core.domain.dto.User
import kotlin.String

fun LoginSuccessfulRequestSerializable.toDomain(): LoginSuccessfulRequest {
    return LoginSuccessfulRequest(
        token = token,
        user = user?.toDomain()
    )
}

fun UserSerializable.toDomain(): User {
    return User(
        id = id,
        email = email,
        phone_number = phone_number,
        first_name = first_name,
        last_name = last_name,
        role = role,
        is_2fa_enabled = is_2fa_enabled,
        is_active = is_active,
        created_at = created_at,
        clinics = clinics,
    )
}

fun ModuleSerializable.toDomain(): Module {
    return Module(
        module_name = module_name,
        module_id = module_id
    )
}

fun LoginSuccessfulRequest.toSerializable(): LoginSuccessfulRequestSerializable {
    return LoginSuccessfulRequestSerializable(
        token = token,
        user = user?.toSerializable()
    )
}

fun User.toSerializable(): UserSerializable {
    return UserSerializable(
        id = id,
        email = email,
        phone_number = phone_number,
        first_name = first_name,
        last_name = last_name,
        role = role,
        is_2fa_enabled = is_2fa_enabled,
        is_active = is_active,
        created_at = created_at,
        clinics = clinics,
    )
}

fun Module.toSerializable(): ModuleSerializable {
    return ModuleSerializable(
        module_name = module_name,
        module_id = module_id
    )
}