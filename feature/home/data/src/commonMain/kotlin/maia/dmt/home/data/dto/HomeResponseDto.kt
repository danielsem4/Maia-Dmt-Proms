package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(
    val modules: List<ModuleDto> = emptyList(),
    val evaluations: List<EvaluationDto> = emptyList(),
)
