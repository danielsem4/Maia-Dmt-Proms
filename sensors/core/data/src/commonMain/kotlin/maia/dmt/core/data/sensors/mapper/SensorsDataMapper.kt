package maia.dmt.core.data.sensors.mapper

import maia.dmt.core.data.dto.sensors.RawSensorReadingDto
import maia.dmt.core.data.dto.sensors.SensorsDataDto
import maia.dmt.core.data.dto.sensors.SensorsDataServerRequest
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.AnomalyEvent
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.SensorsData

fun SensorsData.toDto(): SensorsDataDto {
    return SensorsDataDto(
        avgFrequency = this.avgFrequency,
        stdDevX = this.stdDevX,
        stdDevZ = this.stdDevZ,
        threshold = this.threshold,
        rangeX = this.rangeX,
        rangeZ = this.rangeZ,
        rangeGyroX = this.rangeGyroX,
        rangeGyroZ = this.rangeGyroZ,
        steps = this.steps,
        stdDevSteps = this.stdDevSteps,
        stdDevDeletions = this.stdDevDeletions,
        rangeDeletions = this.rangeDeletions
    )
}

fun Acceleration.toRawDto(): RawSensorReadingDto {
    return RawSensorReadingDto(x = x, y = y, z = z, timestamp = timestamp)
}

fun Gyroscope.toRawDto(): RawSensorReadingDto {
    return RawSensorReadingDto(x = x, y = y, z = z, timestamp = timestamp)
}

private val EMPTY_SENSORS_DATA_DTO = SensorsDataDto(
    avgFrequency = 0f,
    stdDevX = 0f,
    stdDevZ = 0f,
    threshold = 0f,
    rangeX = 0f,
    rangeZ = 0f,
    rangeGyroX = 0f,
    rangeGyroZ = 0f,
    steps = emptyList(),
    stdDevSteps = 0f,
    stdDevDeletions = 0f,
    rangeDeletions = emptyList()
)

fun AnomalyEvent.toServerRequest(
    patientId: Int,
    clinicId: Int,
    uploadDate: String
): SensorsDataServerRequest {
    return SensorsDataServerRequest(
        patientId = patientId,
        clinicId = clinicId,
        uploadDate = uploadDate,
        eventType = eventType.name,
        data = aggregatedStats?.toDto() ?: EMPTY_SENSORS_DATA_DTO,
        rawAccel = rawAccel.map { it.toRawDto() },
        rawGyro = rawGyro.map { it.toRawDto() }
    )
}
