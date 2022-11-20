package org.digital.tracking.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import org.digital.tracking.BuildConfig
import org.digital.tracking.R


object PlayStoreUtil {

    fun rateApp(activity: Activity?) {
        activity?.let {
            val playStoreMarketUri = "market://details?id="
            val playStoreAppUri = "http://play.google.com/store/apps/details?id="
            val uri: Uri = Uri.parse(playStoreMarketUri + activity.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                activity.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playStoreAppUri + activity.packageName)))
            }
        }
    }

    fun shareApp(activity: Activity?) {
        activity?.let {
            val appName = activity.getString(R.string.app_name)
            val appLinkString =
                "Hey check out this app $appName at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            ShareCompat.IntentBuilder(activity)
                .setText(appLinkString)
                .setType("text/plain")
                .setChooserTitle(activity.getString(R.string.app_name))
                .startChooser()
        }
    }

    fun shareLink(context: Context, link: String) {
        ShareCompat.IntentBuilder(context)
            .setText(link)
            .setType("text/plain")
            .setChooserTitle("Link")
            .startChooser()
    }

    fun openLinkInBrowsers(activity: Activity?, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        activity?.startActivity(intent)
    }

    fun helpAndFeedbackViaEmail(activity: Activity?, email: String, subject: String) {
        try {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            emailIntent.type = ClipDescription.MIMETYPE_TEXT_PLAIN
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            activity?.startActivity(Intent.createChooser(emailIntent, "Send mail using..."))
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun helpAndFeedbackViaCall(activity: Activity?, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        activity?.startActivity(intent)
    }

}