package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModuleDto(
    val module_name: String = "",
    val module_id: Int = 0,
)
