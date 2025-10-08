package maia.dmt.medication.domain.medications

interface MedicationService {

    suspend fun getMedications(clinicId: Int, patientId: Int)

    suspend fun reportMedication(clinicId: Int, patientId: Int)

    suspend fun setMedicationReminders(clinicId: Int, patientId: Int)

}