package maia.dmt.statistics.domain.model

data class PatientEvaluationGraphs(
    val patient: PatientEvaluationGraphAuth,
    val measurements_data: Map<String, MeasurementDataWrapper>,
    val medications: List<EvaluationGraphsMedication>? = null,
    val show_medications: Boolean
)

data class PatientEvaluationGraphAuth(
    val id: Int,
    val name: String
)

data class MeasurementDataWrapper(
    val measurement: PatientEvaluationGraphsInfo,
    val data: Map<String, XYData>
)

data class PatientEvaluationGraphsInfo(
    val id: Int,
    val name: String
)

data class XYData(
    val x: List<String>,
    val y: List<String>
)

data class EvaluationGraphsMedication(
    val id: String,
    val name: String,
    val form: String,
    val dosage: String,
    val time_taken: String
)
