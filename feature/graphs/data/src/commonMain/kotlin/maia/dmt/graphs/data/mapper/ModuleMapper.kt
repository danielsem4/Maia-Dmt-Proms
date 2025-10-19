package maia.dmt.graphs.data.mapper

import maia.dmt.graphs.data.dto.ChartDataDto
import maia.dmt.graphs.data.dto.ChartResponseDto
import maia.dmt.graphs.data.dto.MeasurementDto
import maia.dmt.graphs.data.dto.PatientDto
import maia.dmt.graphs.domain.models.ChartData
import maia.dmt.graphs.domain.models.ChartResponse
import maia.dmt.graphs.domain.models.Measurement
import maia.dmt.graphs.domain.models.Patient

fun ChartResponseDto.toDomain(): ChartResponse {
    return ChartResponse(
        patient = patient.toDomain(),
        measurement = measurement.toDomain(),
        bar_charts = bar_charts?.mapValues { it.value.toDomain() },
        line_charts = line_charts?.mapValues { it.value.toDomain() },
        pie_charts = pie_charts?.mapValues { it.value.toDomain() }
    )
}

fun PatientDto.toDomain(): Patient {
    return Patient(
        id = id,
        name = name
    )
}

fun MeasurementDto.toDomain(): Measurement {
    return Measurement(
        id = id,
        name = name
    )
}

fun ChartDataDto.toDomain(): ChartData {
    return ChartData(
        dates = dates,
        values = values
    )
}

