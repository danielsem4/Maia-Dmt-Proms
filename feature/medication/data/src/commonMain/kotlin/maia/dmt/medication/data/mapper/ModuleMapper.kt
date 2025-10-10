package maia.dmt.medication.data.mapper

import maia.dmt.medication.data.dto.MedicationDto
import maia.dmt.medication.data.dto.MedicationReportDto
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.domain.models.MedicationReport

fun MedicationDto.toDomain(): Medication {
    return Medication(
        id = id,
        patient_id = patient_id,
        medicine_id = medicine_id,
        name = name,
        form = form,
        unit = unit,
        frequency = frequency,
        frequency_data = frequency_data,
        start_date = start_date,
        end_date = end_date,
        dosage = dosage
    )
}

fun MedicationReport.toSerial(): MedicationReportDto {
    return MedicationReportDto(
        clinic_id = clinic_id,
        patient_id = patient_id,
        medication_id = medication_id,
        timestamp = timestamp
    )
}