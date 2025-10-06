package maia.dmt.home.data.mapper

import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.domain.models.Module

fun ModuleDto.toDomain(): Module {
    return Module(
        module_name = module_name,
        module_id = module_id,
    )
}