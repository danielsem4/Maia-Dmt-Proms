package maia.dmt.medication.data.medications

import io.ktor.client.HttpClient
import maia.dmt.medication.domain.medications.MedicationService

class KtorMedicationService(
private val httpClient: HttpClient,
) : MedicationService {

    override suspend fun getMedications(clinicId: Int, patientId: Int) {

    }

    override suspend fun reportMedication(clinicId: Int, patientId: Int) {

    }

    override suspend fun setMedicationReminders(clinicId: Int, patientId: Int) {

    }


}