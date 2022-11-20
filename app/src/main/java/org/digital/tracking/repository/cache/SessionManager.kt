package org.digital.tracking.repository.cache

import android.app.Activity
import org.digital.tracking.auth.UserAuthActivity
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.utils.navigateToActivity
import org.digital.tracking.utils.showToast

object SessionManager {

    fun sessionExpired(activity: Activity, sharedPrefs: SharedPrefs) {
        logoutUser(activity, sharedPrefs)
        activity.showToast("Session Expired")
    }

    fun logoutUser(activity: Activity, sharedPrefs: SharedPrefs) {
        UserCacheManager.removeUserSession(sharedPrefs)
        activity.finishAffinity()
        activity.navigateToActivity(UserAuthActivity::class.java)
    }
}