package maia.dmt.proms

import android.app.Application
import maia.dmt.proms.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class DmtApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@DmtApplication)
            androidLogger()

        }
    }
}