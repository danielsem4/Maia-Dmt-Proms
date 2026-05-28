package maia.dmt.onoffstate.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [ActivitySampleEntity::class, BaselineEntity::class],
    version = 1
)
@ConstructedBy(OnOffStateDatabaseConstructor::class)
abstract class OnOffStateDatabase : RoomDatabase() {
    abstract fun onOffStateDao(): OnOffStateDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object OnOffStateDatabaseConstructor : RoomDatabaseConstructor<OnOffStateDatabase> {
    override fun initialize(): OnOffStateDatabase
}
