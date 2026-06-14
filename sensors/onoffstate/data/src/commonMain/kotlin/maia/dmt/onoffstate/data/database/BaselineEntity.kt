package maia.dmt.onoffstate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_baseline")
data class BaselineEntity(
    @PrimaryKey val id: Int = 1,
    val avgAccelMagnitude: Float,
    val stdDevAccelMagnitude: Float,
    val avgGyroMagnitude: Float,
    val stdDevGyroMagnitude: Float,
    val avgStepFrequency: Float,
    val stdDevStepFrequency: Float,
    val sampleCount: Int,
    val calibrationComplete: Boolean,
    val lastUpdated: Long
)
