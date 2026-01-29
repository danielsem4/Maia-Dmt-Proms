package maia.dmt.fallandshake.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import kotlin.math.pow
import kotlin.math.sqrt

// detect Fall based on Peak Acceleration
class DetectFallUseCase {
    private val FALL_THRESHOLD = 30.0f

    fun execute(dataList: List<Acceleration>): Boolean {
        if (dataList.isEmpty()) return false

        val peakAcceleration = dataList.maxOfOrNull {
            sqrt(it.x.pow(2) + it.y.pow(2) + it.z.pow(2))
        } ?: 0f

        return peakAcceleration > FALL_THRESHOLD
    }
}

// average Data (Window Size 50)
class ProcessAccelerometerDataUseCase {

    fun execute(dataList: List<Acceleration>): List<Acceleration> {
        val windowSize = 50
        val averagedData = mutableListOf<Acceleration>()

        var windowSumX = 0f
        var windowSumY = 0f
        var windowSumZ = 0f
        var count = 0

        dataList.forEach { item ->
            windowSumX += item.x
            windowSumY += item.y
            windowSumZ += item.z
            count++

            if (count == windowSize) {
                averagedData.add(
                    Acceleration(
                        x = windowSumX / count,
                        y = windowSumY / count,
                        z = windowSumZ / count,
                        timestamp = item.timestamp
                    )
                )
                windowSumX = 0f
                windowSumY = 0f
                windowSumZ = 0f
                count = 0
            }
        }
        return averagedData
    }
}