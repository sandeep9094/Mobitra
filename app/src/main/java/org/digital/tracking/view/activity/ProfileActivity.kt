package org.digital.tracking.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.auth.UserAuthActivity
import org.digital.tracking.databinding.ActivityProfileBinding
import org.digital.tracking.di.AppModule
import org.digital.tracking.model.User
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.navigateToActivity
import androidx.fragment.app.activityViewModels
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.cache.SessionManager
import org.digital.tracking.viewModel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {

    private var user: User? = null

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        setContentView(binding.root)

        initToolbar(binding.root, getString(R.string.profile))

        if (UserCacheManager.getUser() == null) {
            viewModel.fetchUser()
        } else {
            user = UserCacheManager.getUser()
            binding.user = user
            binding.lifecycleOwner = this
        }

        binding.logoutButton.setOnClickListener {
            SessionManager.logoutUser(this, sharedPrefs)
        }

        binding.addDeviceTextView.setOnClickListener {
            navigateToActivity(AddDeviceActivity::class.java)
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.userResult.observe(this) {
            when (it) {
                is ApiResult.Success -> {
                    binding.user = it.data.payload
                    binding.lifecycleOwner = this
                    UserCacheManager.setUser(it.data.payload)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }


}