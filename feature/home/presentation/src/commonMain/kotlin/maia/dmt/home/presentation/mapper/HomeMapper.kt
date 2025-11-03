package maia.dmt.home.presentation.mapper

import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.activities_icon
import dmtproms.feature.home.presentation.generated.resources.clock_icon
import dmtproms.feature.home.presentation.generated.resources.evaluation_icon
import dmtproms.feature.home.presentation.generated.resources.file_upload_icon
import dmtproms.feature.home.presentation.generated.resources.hitber_icon
import dmtproms.feature.home.presentation.generated.resources.medications_icon
import dmtproms.feature.home.presentation.generated.resources.memory_icon
import dmtproms.feature.home.presentation.generated.resources.module_activities
import dmtproms.feature.home.presentation.generated.resources.module_cdt
import dmtproms.feature.home.presentation.generated.resources.module_document_share
import dmtproms.feature.home.presentation.generated.resources.module_hitber
import dmtproms.feature.home.presentation.generated.resources.module_measurements
import dmtproms.feature.home.presentation.generated.resources.module_medications
import dmtproms.feature.home.presentation.generated.resources.module_memory
import dmtproms.feature.home.presentation.generated.resources.module_orientation
import dmtproms.feature.home.presentation.generated.resources.module_settings
import dmtproms.feature.home.presentation.generated.resources.module_statistics
import dmtproms.feature.home.presentation.generated.resources.orientation_icon
import dmtproms.feature.home.presentation.generated.resources.settings_icon
import dmtproms.feature.home.presentation.generated.resources.statistics_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

fun mapModuleIcon(moduleName: String): DrawableResource {
    return when (moduleName.lowercase()) {
        "document share" -> Res.drawable.file_upload_icon
        "measurements" -> Res.drawable.evaluation_icon
        "medications" -> Res.drawable.medications_icon
        "activities" -> Res.drawable.activities_icon
        "memory" -> Res.drawable.memory_icon
        "cdt" -> Res.drawable.clock_icon
        "orientation" -> Res.drawable.orientation_icon
        "hitber" -> Res.drawable.hitber_icon
        "statistics" -> Res.drawable.statistics_icon
        "settings" -> Res.drawable.settings_icon
        else -> Res.drawable.hitber_icon
    }
}

fun mapModuleNameResource(moduleName: String): StringResource {
    return when (moduleName.lowercase()) {
        "document share" -> Res.string.module_document_share
        "measurements" -> Res.string.module_measurements
        "medications" -> Res.string.module_medications
        "activities" -> Res.string.module_activities
        "memory" -> Res.string.module_memory
        "cdt" -> Res.string.module_cdt
        "orientation" -> Res.string.module_orientation
        "hitber" -> Res.string.module_hitber
        "statistics" -> Res.string.module_statistics
        "settings" -> Res.string.module_settings
        else -> Res.string.module_hitber
    }
}