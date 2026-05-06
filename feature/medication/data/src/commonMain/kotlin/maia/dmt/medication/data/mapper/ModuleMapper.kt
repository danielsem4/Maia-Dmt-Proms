package maia.dmt.medication.data.mapper

import kotlinx.serialization.json.Json
import maia.dmt.medication.data.dto.MedicationDto
import maia.dmt.medication.data.dto.MedicationReportDto
import maia.dmt.medication.data.dto.FrequencyDataDto
import maia.dmt.medication.data.dto.ReportedMedicationDto
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.domain.models.MedicationReport
import maia.dmt.medication.domain.models.ReportedMedication

fun MedicationDto.toDomain(): Medication {
    return Medication(
        id = id,
        patient_id = patient,
        medicine_id = medication,
        name = med_name,
        form = med_form,
        unit = med_unit,
        frequency = frequency,
        frequency_data = Json.encodeToString(frequency_data),
        start_date = start_date,
        end_date = end_date,
        dosage = dosage
    )
}

fun MedicationReport.toSerial(): MedicationReportDto {
    return MedicationReportDto(
        taken_at = taken_at,
        dosage_taken = dosage_taken,
        status = status
    )
}

fun ReportedMedicationDto.toDomain(): ReportedMedication {
    return ReportedMedication(
        id = id,
        patientMedication = patient_medication,
        medName = med_name,
        takenAt = taken_at,
        dosageTaken = dosage_taken,
        status = status
    )
}

fun ReportedMedication.toSerial(): ReportedMedicationDto {
    return ReportedMedicationDto(
        id = id,
        patient_medication = patientMedication,
        med_name = medName,
        taken_at = takenAt,
        dosage_taken = dosageTaken,
        status = status
    )
}