package org.digital.tracking.di

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import org.digital.tracking.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val defaultError: String = context.getString(R.string.error_message_default)
    val sessionExpired: String = "sessionExpired"

    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

}