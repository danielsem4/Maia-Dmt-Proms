package maia.dmt.onoffstate.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getOnOffStateDatabaseBuilder(): RoomDatabase.Builder<OnOffStateDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/onoff_state.db"
    return Room.databaseBuilder<OnOffStateDatabase>(
        name = dbFilePath
    )
}
