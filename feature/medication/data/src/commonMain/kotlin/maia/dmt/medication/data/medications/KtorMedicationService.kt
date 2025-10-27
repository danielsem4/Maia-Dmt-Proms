package maia.dmt.medication.data.medications

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.medication.data.dto.MedicationDto
import maia.dmt.medication.data.dto.ReportedMedicationDto
import maia.dmt.medication.data.mapper.toDomain
import maia.dmt.medication.data.mapper.toSerial
import maia.dmt.medication.domain.medications.MedicationService
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.domain.models.MedicationNotification
import maia.dmt.medication.domain.models.MedicationReport
import maia.dmt.medication.domain.models.ReportedMedication

class KtorMedicationService(
    private val httpClient: HttpClient,
) : MedicationService {

    override suspend fun getMedications(
        clinicId: Int,
        patientId: Int
    ): Result<List<Medication>, DataError.Remote> {
        return httpClient.get<List<MedicationDto>>(
            route = "Medication_list/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId
            )
        ).map { it.map { it.toDomain() } }

    }

    override suspend fun reportMedication(body: MedicationReport): EmptyResult<DataError.Remote> {

        return httpClient.post(
            route = "report_medication/",
            body = body.toSerial()
        )

    }

    override suspend fun setMedicationReminders(body: MedicationNotification): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "patientNotificationData/",
            body = body
        )
    }

    override suspend fun getAllReportedMedications(
        patientId: Int,
        clinicId: Int
    ): Result<List<ReportedMedication>, DataError.Remote> {
        return httpClient.get<List<ReportedMedicationDto>>(
            route = "getPatientReports/$clinicId/$patientId/",
            queryParams = mapOf(
                "report_type" to "medications"
            )
        ).map { it.map { it.toDomain() } }
    }

}