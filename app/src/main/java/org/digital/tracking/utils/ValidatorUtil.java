package org.digital.tracking.utils;

import android.util.Patterns;

public class ValidatorUtil {

    public static Boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
