package maia.dmt.core.data.sensors.mapper

import maia.dmt.core.data.dto.sensors.SensorsDataDto
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