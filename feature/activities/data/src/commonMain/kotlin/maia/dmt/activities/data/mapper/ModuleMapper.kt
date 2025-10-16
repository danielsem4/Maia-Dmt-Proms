package maia.dmt.activities.data.mapper

import maia.dmt.activities.data.dto.ActivityItemDto
import maia.dmt.activities.data.dto.ActivityItemReportDto
import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.domain.model.ActivityItemReport

fun ActivityItemReportDto.toDomain(): ActivityItemReport {
    return ActivityItemReport(
        clinic_id = clinic_id,
        patient_id = patient_id,
        activity_id = activity_id,
        date = date
    )
}

fun ActivityItemReport.toSerial(): ActivityItemReportDto {
    return ActivityItemReportDto(
        clinic_id = clinic_id,
        patient_id = patient_id,
        activity_id = activity_id,
        date = date
    )
}

fun ActivityItemDto.toDomain(): ActivityItem {
    return ActivityItem(
        id = id,
        name = name,
        description = description
    )
}