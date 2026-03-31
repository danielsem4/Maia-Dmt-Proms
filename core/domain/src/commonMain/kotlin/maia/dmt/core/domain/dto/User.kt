package maia.dmt.core.domain.dto
data class User(
    val id: String = "",
    val email: String = "",
    val phone_number: String? = null,
    val first_name: String = "",
    val last_name: String = "",
    val role: String = "",
    val is_2fa_enabled: Boolean = false,
    val is_active: Boolean = true,
    val created_at: String = "",
    val clinics: List<String> = emptyList(),
)
