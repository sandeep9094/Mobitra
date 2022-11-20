package org.digital.tracking.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityMainBinding
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.manager.PermissionManager
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.cache.SessionManager
import org.digital.tracking.utils.openPermissionSettings
import org.digital.tracking.utils.showToast
import org.digital.tracking.viewModel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    companion object {
        lateinit var bottomNavigationView: BottomNavigationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        checkLocationPermission()

        mainViewModel.fetchUser()
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.userResult.observe(this) {
            when (it) {
                is ApiResult.Error -> {
                    if (it.errorMessage == resourceProvider.sessionExpired) {
                        SessionManager.sessionExpired(this, sharedPrefs)
                    }
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    private fun checkLocationPermission() {
        PermissionManager.instance().requestPermissions(this,
            PERMISSIONS,
            object : PermissionManager.PermissionCallback {
                override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String?>, grantResults: IntArray) {
                    grantResults.forEach {
                        if (it == PackageManager.PERMISSION_DENIED) {
                            showToast(R.string.error_message_location_permission_required)
                            openPermissionSettings()
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