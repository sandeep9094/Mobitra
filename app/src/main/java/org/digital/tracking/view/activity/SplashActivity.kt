package org.digital.tracking.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.auth.UserAuthActivity
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.utils.navigateToActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openNextScreen()
    }

    private fun openNextScreen() {
        if (sharedPrefs.isUserFirstTime) {
            navigateToActivity(OnboardingActivity::class.java)
        } else if (sharedPrefs.isUserLoggedIn) {
            navigateToActivity(MainActivity::class.java)
        } else {
            navigateToActivity(UserAuthActivity::class.java)
        }
    }

}