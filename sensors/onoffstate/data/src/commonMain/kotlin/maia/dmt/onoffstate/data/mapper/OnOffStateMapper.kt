package maia.dmt.onoffstate.data.mapper

import maia.dmt.onoffstate.data.database.ActivitySampleEntity
import maia.dmt.onoffstate.data.database.BaselineEntity
import maia.dmt.onoffstate.domain.model.ActivityMetrics
import maia.dmt.onoffstate.domain.model.PatientBaseline

fun ActivityMetrics.toEntity(): ActivitySampleEntity = ActivitySampleEntity(
    avgAccelMagnitude = avgAccelMagnitude,
    accelVariability = accelVariability,
    avgGyroMagnitude = avgGyroMagnitude,
    stepFrequency = stepFrequency,
    windowDurationMs = windowDurationMs,
    timestamp = timestamp
)

fun ActivitySampleEntity.toDomain(): ActivityMetrics = ActivityMetrics(
    avgAccelMagnitude = avgAccelMagnitude,
    accelVariability = accelVariability,
    avgGyroMagnitude = avgGyroMagnitude,
    stepFrequency = stepFrequency,
    windowDurationMs = windowDurationMs,
    timestamp = timestamp
)

fun PatientBaseline.toEntity(): BaselineEntity = BaselineEntity(
    avgAccelMagnitude = avgAccelMagnitude,
    stdDevAccelMagnitude = stdDevAccelMagnitude,
    avgGyroMagnitude = avgGyroMagnitude,
    stdDevGyroMagnitude = stdDevGyroMagnitude,
    avgStepFrequency = avgStepFrequency,
    stdDevStepFrequency = stdDevStepFrequency,
    sampleCount = sampleCount,
    calibrationComplete = calibrationComplete,
    lastUpdated = lastUpdated
)

fun BaselineEntity.toDomain(): PatientBaseline = PatientBaseline(
    avgAccelMagnitude = avgAccelMagnitude,
    stdDevAccelMagnitude = stdDevAccelMagnitude,
    avgGyroMagnitude = avgGyroMagnitude,
    stdDevGyroMagnitude = stdDevGyroMagnitude,
    avgStepFrequency = avgStepFrequency,
    stdDevStepFrequency = stdDevStepFrequency,
    sampleCount = sampleCount,
    calibrationComplete = calibrationComplete,
    lastUpdated = lastUpdated
)
