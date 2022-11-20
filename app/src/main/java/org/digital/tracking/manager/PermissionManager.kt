package org.digital.tracking.manager

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionManager {
    private var callback: PermissionCallback? = null

    fun requestPermissions(activity: Activity?, permissions: Array<out String>, callback: PermissionCallback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val count = permissions.size
            val grantResults = IntArray(count)
            for (i in permissions.indices) {
                grantResults[i] = PackageManager.PERMISSION_GRANTED
            }
            callback.onRequestPermissionsResult(9999, permissions, grantResults)
        } else {
            this.callback = callback
            activity?.requestPermissions(permissions, 9999)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        callback?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    interface PermissionCallback {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String?>, grantResults: IntArray)
    }

    companion object {
        private val manager = PermissionManager()
        fun instance(): PermissionManager {
            return manager
        }
    }
}