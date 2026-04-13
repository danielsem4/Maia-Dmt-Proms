package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModuleDto(
    val id: String = "",
    val clinic: String = "",
    val module: String = "",
    val module_name: String = "",
    val module_description: String = "",
    val is_active: Boolean = true,
)
