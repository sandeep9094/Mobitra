package org.digital.tracking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.digital.tracking.manager.SharedPrefs
import timber.log.Timber

@HiltAndroidApp
class DigitalTrackingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}