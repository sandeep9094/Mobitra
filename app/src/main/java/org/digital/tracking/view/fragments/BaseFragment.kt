package org.digital.tracking.view.fragments

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.digital.tracking.R
import org.digital.tracking.manager.PermissionManager
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.ProfileActivity
import java.util.*

abstract class BaseFragment : Fragment() {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    open fun initToolbar(view: View, title: String) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val leftIcon = view.findViewById<ImageView>(R.id.left_action)
        toolbarTitle.text = title
        leftIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun initToolbar(view: View, @StringRes title: Int) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val leftIcon = view.findViewById<ImageView>(R.id.left_action)
        toolbarTitle.setText(title)
        leftIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun initToolbarReports(view: View, title: String, rightIconClickListener: View.OnClickListener? = null) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val leftIcon = view.findViewById<ImageView>(R.id.left_action)
        val rightIcon = view.findViewById<ImageView>(R.id.right_action)
        toolbarTitle.text = title
        leftIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        rightIcon.makeVisible()
        rightIcon.setOnClickListener(rightIconClickListener)
        context?.let {
            rightIcon.setDrawable(it, R.drawable.ic_baseline_filter_list_24)
        }
    }

    fun initHomeToolbar(view: View) {
        val notifications = view.findViewById<ImageView>(R.id.menuNotifications)
        val menuAccount = view.findViewById<ImageView>(R.id.menuAccount)
        menuAccount.setOnClickListener {
            context.navigateToActivity(ProfileActivity::class.java)
        }
        notifications.setOnClickListener {
            navigateToFragment(R.id.notificationFragment)
        }
    }

    private fun checkLocationPermission() {
        PermissionManager.instance().requestPermissions(activity,
            PERMISSIONS,
            object : PermissionManager.PermissionCallback {
                override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String?>, grantResults: IntArray) {
                    grantResults.forEach {
                        if (it == PackageManager.PERMISSION_DENIED) {
                            showToast(R.string.error_message_location_permission_required)
                            activity?.openPermissionSettings()
                            return
                        }
                    }
                    //Permission Granted
                }

            })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.instance().onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}