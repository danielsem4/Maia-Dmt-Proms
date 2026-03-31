package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.AuthResponseSerializable
import maia.dmt.core.data.dto.AuthTokensSerializable
import maia.dmt.core.data.dto.ClinicSerializable
import maia.dmt.core.data.dto.LoginSuccessfulRequestSerializable
import maia.dmt.core.data.dto.ModuleSerializable
import maia.dmt.core.data.dto.UserSerializable
import maia.dmt.core.domain.dto.AuthResult
import maia.dmt.core.domain.dto.AuthTokens
import maia.dmt.core.domain.dto.Clinic
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.dto.Module
import maia.dmt.core.domain.dto.User

fun AuthTokensSerializable.toDomain(): AuthTokens {
    return AuthTokens(access = access, refresh = refresh)
}

fun AuthTokens.toSerializable(): AuthTokensSerializable {
    return AuthTokensSerializable(access = access, refresh = refresh)
}

fun ClinicSerializable.toDomain(): Clinic {
    return Clinic(
        id = id,
        clinicName = clinic_name,
        clinicUrl = clinic_url,
        clinicImageUrl = clinic_image_url,
        isResearchClinic = is_research_clinic
    )
}

fun Clinic.toSerializable(): ClinicSerializable {
    return ClinicSerializable(
        id = id,
        clinic_name = clinicName,
        clinic_url = clinicUrl,
        clinic_image_url = clinicImageUrl,
        is_research_clinic = isResearchClinic
    )
}

fun AuthResponseSerializable.toAuthResult(): AuthResult {
    return when {
        requires_2fa -> AuthResult.TwoFactorRequired(userId = user_id ?: "")
        requires_clinic_selection -> AuthResult.ClinicSelectionRequired(
            userId = user_id ?: "",
            clinics = clinics?.map { it.toDomain() } ?: emptyList()
        )
        else -> AuthResult.Authenticated(
            tokens = token?.toDomain() ?: AuthTokens("", ""),
            user = user?.toDomain() ?: User()
        )
    }
}

fun LoginSuccessfulRequestSerializable.toDomain(): LoginSuccessfulRequest {
    return LoginSuccessfulRequest(
        tokens = tokens?.toDomain(),
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
        clinics = clinics.map { it.toDomain() },
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
        tokens = tokens?.toSerializable(),
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
        clinics = clinics.map { it.toSerializable() },
    )
}

fun Module.toSerializable(): ModuleSerializable {
    return ModuleSerializable(
        module_name = module_name,
        module_id = module_id
    )
}
