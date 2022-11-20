package org.digital.tracking.utils

import android.util.Patterns

fun String?.isEmailValid(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isPhoneValid(): Boolean {
    if (this.isNullOrEmpty() || this.isEmpty() || this.length < 10) {
        return false
    }
    return Patterns.PHONE.matcher(this).matches()
}