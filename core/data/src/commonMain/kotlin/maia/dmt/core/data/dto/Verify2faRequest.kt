package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Verify2faRequest(
    val code: String,
    val user_id: String
)
