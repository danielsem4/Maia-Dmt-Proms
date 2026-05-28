package maia.dmt.onoffstate.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OnOffStateDao {

    @Insert
    suspend fun insertSample(sample: ActivitySampleEntity)

    @Query("SELECT COUNT(*) FROM activity_samples")
    suspend fun getSampleCount(): Int

    @Query("SELECT * FROM activity_samples ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSamples(limit: Int): List<ActivitySampleEntity>

    @Query("SELECT * FROM activity_samples")
    suspend fun getAllSamples(): List<ActivitySampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBaseline(baseline: BaselineEntity)

    @Query("SELECT * FROM patient_baseline WHERE id = 1")
    suspend fun getBaseline(): BaselineEntity?

    @Query("DELETE FROM activity_samples WHERE timestamp < :cutoff")
    suspend fun deleteOldSamples(cutoff: Long)
}
