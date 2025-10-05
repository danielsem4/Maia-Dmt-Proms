package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.LoginSuccessfulRequestSerializable
import maia.dmt.core.data.dto.ModuleSerializable
import maia.dmt.core.data.dto.UserSerializable
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.dto.Module
import maia.dmt.core.domain.dto.User

fun LoginSuccessfulRequestSerializable.toDomain(): LoginSuccessfulRequest {
    return LoginSuccessfulRequest(
        token = token,
        user = user?.toDomain()

    )
}

fun UserSerializable.toDomain(): User {
    return User(
        id = id,
        password = password,
        last_login = last_login,
        is_superuser = is_superuser,
        is_staff = is_staff,
        is_active = is_active,
        date_joined = date_joined,
        email = email,
        phone_number = phone_number,
        first_name = first_name,
        last_name = last_name,
        is_clinic_manager = is_clinic_manager,
        is_doctor = is_doctor,
        is_patient = is_patient,
        is_research_patient = is_research_patient,
        groups = groups,
        user_permissions = user_permissions,
        clinicId = clinicId,
        clinicName = clinicName,
        modules = this.modules.map { it.toDomain() },
        status = status,
        server_url = server_url
    )
}

fun ModuleSerializable.toDomain(): Module {
    return Module(
        module_name = module_name,
        module_id = module_id
    )
}