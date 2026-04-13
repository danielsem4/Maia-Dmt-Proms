package maia.dmt.home.presentation.mapper

import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.activities_icon
import dmtproms.feature.home.presentation.generated.resources.clock_icon
import dmtproms.feature.home.presentation.generated.resources.evaluation_icon
import dmtproms.feature.home.presentation.generated.resources.file_upload_icon
import dmtproms.feature.home.presentation.generated.resources.hitber_icon
import dmtproms.feature.home.presentation.generated.resources.market_icon
import dmtproms.feature.home.presentation.generated.resources.medications_icon
import dmtproms.feature.home.presentation.generated.resources.memory_icon
import dmtproms.feature.home.presentation.generated.resources.module_activities
import dmtproms.feature.home.presentation.generated.resources.module_cdt
import dmtproms.feature.home.presentation.generated.resources.module_document_share
import dmtproms.feature.home.presentation.generated.resources.module_fmpt
import dmtproms.feature.home.presentation.generated.resources.module_hitber
import dmtproms.feature.home.presentation.generated.resources.module_market
import dmtproms.feature.home.presentation.generated.resources.module_measurements
import dmtproms.feature.home.presentation.generated.resources.module_medications
import dmtproms.feature.home.presentation.generated.resources.module_memory
import dmtproms.feature.home.presentation.generated.resources.module_new_report
import dmtproms.feature.home.presentation.generated.resources.module_orientation
import dmtproms.feature.home.presentation.generated.resources.module_sensors
import dmtproms.feature.home.presentation.generated.resources.module_settings
import dmtproms.feature.home.presentation.generated.resources.module_statistics
import dmtproms.feature.home.presentation.generated.resources.orientation_icon
import dmtproms.feature.home.presentation.generated.resources.phone
import dmtproms.feature.home.presentation.generated.resources.settings_icon
import dmtproms.feature.home.presentation.generated.resources.statistics_icon
import maia.dmt.core.presentation.util.UiText
import org.jetbrains.compose.resources.DrawableResource

fun mapModuleIcon(moduleName: String): DrawableResource {
    return when (moduleName.lowercase()) {
        "document share" -> Res.drawable.file_upload_icon
        "questionnaires", "measurements" -> Res.drawable.evaluation_icon
        "medications" -> Res.drawable.medications_icon
        "activities" -> Res.drawable.activities_icon
        "memory" -> Res.drawable.memory_icon
        "cdt" -> Res.drawable.clock_icon
        "orientation" -> Res.drawable.orientation_icon
        "hitber" -> Res.drawable.hitber_icon
        "statistics" -> Res.drawable.statistics_icon
        "pass" -> Res.drawable.phone
        "settings" -> Res.drawable.settings_icon
        "parkinson sensors" -> Res.drawable.statistics_icon
        "parkinson report" -> Res.drawable.file_upload_icon
        "market test" -> Res.drawable.market_icon
        else -> Res.drawable.hitber_icon
    }
}

fun mapModuleNameToUiText(moduleName: String): UiText {
    return when (moduleName.lowercase()) {
        "document share" -> UiText.Resource(Res.string.module_document_share)
        "questionnaires", "measurements" -> UiText.Resource(Res.string.module_measurements)
        "medications" -> UiText.Resource(Res.string.module_medications)
        "activities" -> UiText.Resource(Res.string.module_activities)
        "memory" -> UiText.Resource(Res.string.module_memory)
        "cdt" -> UiText.Resource(Res.string.module_cdt)
        "orientation" -> UiText.Resource(Res.string.module_orientation)
        "hitber" -> UiText.Resource(Res.string.module_hitber)
        "statistics" -> UiText.Resource(Res.string.module_statistics)
        "settings" -> UiText.Resource(Res.string.module_settings)
        "parkinson sensors" -> UiText.Resource(Res.string.module_sensors)
        "pass" -> UiText.Resource(Res.string.module_fmpt)
        "parkinson report" -> UiText.Resource(Res.string.module_new_report)
        "market test" -> UiText.Resource(Res.string.module_market)
        else -> UiText.DynamicString(moduleName)
    }
}