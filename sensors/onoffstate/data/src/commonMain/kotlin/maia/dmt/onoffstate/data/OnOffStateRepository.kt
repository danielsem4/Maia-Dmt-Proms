package maia.dmt.onoffstate.data

import maia.dmt.onoffstate.data.database.OnOffStateDao
import maia.dmt.onoffstate.data.mapper.toDomain
import maia.dmt.onoffstate.data.mapper.toEntity
import maia.dmt.onoffstate.domain.model.ActivityMetrics
import maia.dmt.onoffstate.domain.model.PatientBaseline
import maia.dmt.onoffstate.domain.usecase.ComputeBaselineUseCase

class OnOffStateRepository(
    private val dao: OnOffStateDao,
    private val computeBaseline: ComputeBaselineUseCase
) {

    suspend fun storeSample(metrics: ActivityMetrics) {
        dao.insertSample(metrics.toEntity())
        recomputeBaseline()
    }

    suspend fun getBaseline(): PatientBaseline? {
        return dao.getBaseline()?.toDomain()
    }

    suspend fun getSampleCount(): Int {
        return dao.getSampleCount()
    }

    private suspend fun recomputeBaseline() {
        val samples = dao.getAllSamples().map { it.toDomain() }
        val baseline = computeBaseline.compute(samples)
        dao.upsertBaseline(baseline.toEntity())
    }
}
