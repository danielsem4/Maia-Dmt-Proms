package maia.dmt.activities.data.mapper

import kotlinx.serialization.json.Json
import maia.dmt.activities.data.dto.ActivityItemDto
import maia.dmt.activities.data.dto.ActivityItemReportDto
import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.domain.model.ActivityItemReport

fun ActivityItemDto.toDomain(): ActivityItem {
    return ActivityItem(
        id = id,
        patient = patient,
        clinic = clinic,
        doctor = doctor,
        activity = activity,
        activityName = activity_name,
        activityDescription = activity_description,
        startDate = start_date,
        endDate = end_date,
        frequency = frequency,
        frequencyData = frequency_data?.let { Json.encodeToString(it) }
    )
}

fun ActivityItemReport.toSerial(): ActivityItemReportDto {
    return ActivityItemReportDto(
        time_done = timeDone
    )
}
