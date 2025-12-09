package maia.dmt.core.domain.usecase

class BuildImagePathUseCase {
    operator fun invoke(
        clinicId: String,
        patientId: String,
        measurementId: String,
        pathDate: String,
        progress: String = "",
        fileName: String = "image.png",
        extraData: String? = null
    ): String {
        val formattedDate = pathDate.split(" ").joinToString("%20") {
            it.replace(":", "-").replace(".", "-")
        }
        val parts = listOf(
            "clinics",
            clinicId,
            "patients",
            patientId,
            "measurements",
            measurementId,
            formattedDate,
            progress,
            extraData ?: "",
            fileName
        )
        return parts.filter { it.isNotEmpty() }.joinToString("/")
    }
}