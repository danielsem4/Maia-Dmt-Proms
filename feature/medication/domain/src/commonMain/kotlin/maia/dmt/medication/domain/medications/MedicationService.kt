package maia.dmt.medication.domain.medications

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.domain.models.MedicationNotification
import maia.dmt.medication.domain.models.MedicationReport
import maia.dmt.medication.domain.models.ReportedMedication


interface MedicationService {

    suspend fun getMedications(clinicId: String, patientId: String): Result<List<Medication>, DataError.Remote>

    suspend fun reportMedication(body: MedicationReport): EmptyResult<DataError.Remote>

    suspend fun setMedicationReminders(body: MedicationNotification): EmptyResult<DataError.Remote>

    suspend fun getAllReportedMedications(patientId: String, clinicId: String): Result<List<ReportedMedication>, DataError.Remote>

}


