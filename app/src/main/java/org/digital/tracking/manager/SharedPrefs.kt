package org.digital.tracking.manager

import android.content.Context

class SharedPrefs(context: Context) {

    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val IS_USER_FIRST_TIME = "isUserFirstTime"
    private val IS_USER_LOGGED_IN = "isUserLoggedIn"
    private val AUTH_TOKEN = "authToken"
    private var USER_ID = "userId"
    private var FALLBACK_IMEI = "fallbackImei"

    var authToken: String
        get() = preferences.getString(AUTH_TOKEN, "") ?: ""
        set(value) = preferences.edit().putString(AUTH_TOKEN, value).apply()

    var isUserFirstTime: Boolean
        get() = preferences.getBoolean(IS_USER_FIRST_TIME, true)
        set(value) = preferences.edit().putBoolean(IS_USER_FIRST_TIME, value).apply()

    var isUserLoggedIn: Boolean
        get() = preferences.getBoolean(IS_USER_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(IS_USER_LOGGED_IN, value).apply()

    var userId: String
        get() = preferences.getString(USER_ID, "") ?: ""
        set(value) = preferences.edit().putString(USER_ID, value).apply()

    var fallbackImei: String
        get() = preferences.getString(FALLBACK_IMEI, "") ?: ""
        set(value) = preferences.edit().putString(FALLBACK_IMEI, value).apply()

    fun clearAllKeys() {
        preferences.edit().clear().apply()
    }
}