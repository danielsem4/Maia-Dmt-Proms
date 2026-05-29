package maia.dmt.onoffstate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_samples")
data class ActivitySampleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val avgAccelMagnitude: Float,
    val accelVariability: Float,
    val avgGyroMagnitude: Float,
    val stepFrequency: Float,
    val windowDurationMs: Long,
    val timestamp: Long
)
