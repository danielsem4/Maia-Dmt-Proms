package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModuleSerializable(
    val module_name: String = "",
    val module_id: Int = 0,
)
