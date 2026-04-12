package maia.dmt.home.data.mapper

import maia.dmt.home.data.dto.FcmTokenRequestDto
import maia.dmt.home.data.dto.MeasurementDto
import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.domain.models.FcmTokenRequest
import maia.dmt.home.domain.models.Module

fun ModuleDto.toDomain(): Module {
    return Module(
        id = id,
        name = module_name,
        description = module_description,
        isActive = is_active,
    )
}

fun MeasurementDto.toDomain(): Module {
    return Module(
        id = id,
        name = measurement_name,
        isActive = is_active,
    )
}

fun FcmTokenRequestDto.toDomain(): FcmTokenRequest {
    return FcmTokenRequest(
        user_id = user_id,
        clinic_id = clinic_id,
        fcm_token = fcm_token
    )
}

fun FcmTokenRequest.toSerial(): FcmTokenRequestDto {
    return FcmTokenRequestDto(
        user_id = user_id,
        clinic_id = clinic_id,
        fcm_token = fcm_token
    )
}