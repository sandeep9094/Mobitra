package org.digital.tracking.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.digital.tracking.R
import java.util.*


val emptyString = ""
val nA = "N/A"

fun showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun Context?.navigateToActivity(className: Class<*>) {
    this?.startActivity(Intent(this, className))
}

fun Context?.navigateToActivity(intent: Intent) {
    this?.startActivity(intent)
}


fun Fragment.navigateToFragment(@IdRes resId: Int, bundle: Bundle? = null) {
    val navOptions: NavOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    findNavController().navigate(resId, bundle, navOptions)
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}


fun ImageView.setTintColor(@ColorRes colorResId: Int) {
    this.setColorFilter(ContextCompat.getColor(context, colorResId), android.graphics.PorterDuff.Mode.MULTIPLY);
}

fun Button.setActive() {
    this.isEnabled = true
}

fun Button.setInactive() {
    this.isEnabled = false
}

fun Button.setPrimaryStyle() {
    this.setTextColor(this.resources.getColor(R.color.white))
    this.background = ContextCompat.getDrawable(this.context, R.drawable.bg_button_primary)
}

fun Button.setSecondaryStyle() {
    this.setTextColor(this.resources.getColor(R.color.colorPrimary))
    this.background = ContextCompat.getDrawable(this.context, R.drawable.bg_button_secondary)
}

fun ImageView.setDrawable(context: Context, @DrawableRes resId: Int) {
    this.setImageDrawable(ContextCompat.getDrawable(context, resId))
}

fun Context.showSnackBar(view: View, message: String) {
    Snackbar.make(this, view, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.showSnackBarLong(view: View, message: String) {
    Snackbar.make(this, view, message, Snackbar.LENGTH_LONG).show()
}

fun Context.showSnackBar(view: View, message: String, action: String, listener: View.OnClickListener) {
    val snackBar = Snackbar.make(this, view, message, Snackbar.LENGTH_LONG)
    snackBar.setAction(action, listener)
    snackBar.show()
}


fun Context?.showToast(message: String) {
    this?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}


fun Context?.showToastLong(message: String) {
    this?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context?.showToast(@StringRes resId: Int) {
    this?.let {
        Toast.makeText(this, it.getString(resId), Toast.LENGTH_SHORT).show()
    }
}


fun Fragment?.showToast(message: String) {
    this?.let {
        Toast.makeText(it.context, message, Toast.LENGTH_SHORT).show()
    }
}


fun Fragment?.showToastLong(message: String) {
    this?.let {
        Toast.makeText(it.context, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment?.showToast(@StringRes resId: Int) {
    this?.let {
        Toast.makeText(it.context, it.getString(resId), Toast.LENGTH_SHORT).show()
    }
}


fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }
}

fun EditText.hideKeyboard(activity: Activity?) {
    val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.performHapticFeedback() {
    this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
}

fun String.isValidResponse(): Boolean {
    return this == "true"
}

fun Activity.openPermissionSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Context?.getCompleteAddressString(latitude: Double?, longitude: Double?): String {
    var strAdd = nA
    val geocoder = Geocoder(this, Locale.getDefault())
    try {
        if (latitude == null || longitude == null) {
            return strAdd
        }
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            val returnedAddress: Address = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
            }
            strAdd = strReturnedAddress.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return strAdd
}

fun Float?.getImeiString(): String {
    val imei = this?.toInt() ?: 0
    return if (imei == 0) {
        ""
    } else {
        imei.toString()
    }
}

fun Double?.getSpeedString(): String {
    if (this == null) {
        return "0.0 ${Constants.UNITS_SPEED}"
    }
    return "${this.formatDoubleValue()} ${Constants.UNITS_SPEED}"
}

fun Double?.getDistanceString(): String {
    if (this == null) {
        return "0.0 ${Constants.UNITS_DISTANCE}"
    }
    return "${this.formatDoubleValue()} ${Constants.UNITS_DISTANCE}"
}

fun Double?.formatDoubleValue(): Double {
    var formattedValue = 0.0
    if (this == null) {
        return formattedValue
    }
    formattedValue = "%.2f".format(this).toDouble()
    return formattedValue
}

fun <R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: () -> R,
    onPostExecute: (R) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
        doInBackground()
    }
    onPostExecute(result)
}