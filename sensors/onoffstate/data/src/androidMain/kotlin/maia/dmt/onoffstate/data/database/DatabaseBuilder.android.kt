package maia.dmt.onoffstate.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getOnOffStateDatabaseBuilder(context: Context): RoomDatabase.Builder<OnOffStateDatabase> {
    val dbFile = context.getDatabasePath("onoff_state.db")
    return Room.databaseBuilder<OnOffStateDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
