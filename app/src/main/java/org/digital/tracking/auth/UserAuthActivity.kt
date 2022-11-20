package org.digital.tracking.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.BuildConfig
import org.digital.tracking.view.activity.MainActivity
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityUserAuthBinding
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.ResponseResult
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.BaseActivity
import org.digital.tracking.view.activity.ForgotPasswordActivity
import javax.inject.Inject

@AndroidEntryPoint
class UserAuthActivity : BaseActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    companion object {
        private val TAG = UserAuthActivity::class.java.simpleName
    }

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: ActivityUserAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(this)
        binding.signUpButton.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)
        binding.signUpEmailButton.setOnClickListener(this)
//        binding.loginTabLayout.addOnTabSelectedListener(this)

        setupObservers()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.loginButton.id -> {
//                if (binding.inputEmailLayout.isVisible) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString()
                loginWithEmail(email, password)
//                } else if (binding.inputPhoneLayout.isVisible) {
//                    val phone = binding.phoneEditText.text.toString().trim()
//                    val password = binding.phonePasswordEditText.text.toString()
//                    loginWithPhone(phone, password)
//                }
            }
            binding.signUpButton.id -> {
                navigateToActivity(SignUpPhoneActivity::class.java)
            }
            binding.signUpEmailButton.id -> {
                val intent = Intent(this, SignUpActivity::class.java)
                intent.apply {
                    putExtra(Constants.INTENT_KEY_SIGN_UP_TYPE, SignUpType.EMAIL)
                    putExtra(Constants.INTENT_KEY_EMAIL, "")
                }
                navigateToActivity(intent)
//                navigateToActivity(SignUpEmailActivity::class.java)
            }
            binding.forgotPassword.id -> {
                navigateToActivity(ForgotPasswordActivity::class.java)
            }
        }
    }

    private fun loginWithPhone(phone: String, password: String) {
        if (!phone.isPhoneValid()) {
            binding.phoneEditText.error = getString(R.string.message_error_phone_number)
            binding.phoneEditText.requestFocus()
            return
        } else {
            //If user corrected email, remove error
            binding.phoneEditText.error = null
        }
        if (password.trim().isEmpty()) {
            binding.phonePasswordEditText.error = getString(R.string.error_message_password_empty)
            binding.phonePasswordEditText.requestFocus()
            return
        } else {
            binding.phonePasswordEditText.error = null
        }
        binding.phonePasswordEditText.hideKeyboard(this)
        viewModel.loginUserWithPhone(phone, password)
    }

    private fun loginWithEmail(email: String, password: String) {
        if (email.isEmpty()) {
            binding.emailEditText.error = getString(R.string.error_message_invalid_email)
            binding.emailLayout.requestFocus()
            return
        } else {
            //If user corrected email, remove error
            binding.emailEditText.error = null
        }
        if (password.trim().isEmpty()) {
            binding.passwordEditText.error = getString(R.string.error_message_password_empty)
            binding.passwordEditText.requestFocus()
            return
        } else {
            binding.passwordEditText.error = null
        }
        binding.passwordEditText.hideKeyboard(this)
        viewModel.loginUserWithEmail(email, password)
    }

    private fun openNextActivity() {
        sharedPrefs.isUserLoggedIn = true
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
//        when(binding.loginTabLayout.selectedTabPosition) {
//            //Login with Phone
//            0 -> {
//                binding.inputPhoneLayout.makeVisible()
//                binding.inputEmailLayout.makeInvisible()
//            }
//            //Login with Email
//            1 -> {
        binding.inputPhoneLayout.makeInvisible()
        binding.inputEmailLayout.makeVisible()
//            }
//        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //Do nothing
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        //Do nothing
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.loginButton.isEnabled = false
                }
                is ApiResult.Error -> {
                    binding.loginButton.isEnabled = true
                    showToast(it.errorMessage)
                }
                is ApiResult.Success -> {
                    val response = it.data
                    sharedPrefs.userId = response.userId
                    sharedPrefs.authToken = response.token
                    openNextActivity()
                }
            }
        }
    }


}