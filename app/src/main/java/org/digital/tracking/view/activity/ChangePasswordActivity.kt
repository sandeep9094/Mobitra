package org.digital.tracking.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.auth.UserAuthActivity
import org.digital.tracking.databinding.ActivityChangePasswordBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.utils.Constants
import org.digital.tracking.utils.hideKeyboard
import org.digital.tracking.utils.navigateToActivity
import org.digital.tracking.utils.showToast
import org.digital.tracking.viewModel.ForgotPasswordViewModel

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity(), View.OnClickListener {

    private var emailId: String = ""
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.password))

        emailId = intent.getStringExtra(Constants.INTENT_KEY_EMAIL) ?: ""

        binding.changePasswordButton.setOnClickListener(this)

        setupObservers()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.changePasswordButton.id -> {
                val password = binding.passwordEditText.text.toString()
                if (password.isEmpty()) {
                    binding.passwordEditText.error = getString(R.string.error_message_password_empty)
                    binding.passwordEditText.requestFocus()
                    return
                }
                val confirmPassword = binding.confirmPasswordEditText.text.toString()
                if (confirmPassword.isEmpty()) {
                    binding.confirmPasswordEditText.error = getString(R.string.error_message_password_empty)
                    binding.confirmPasswordEditText.requestFocus()
                    return
                }
                if (password != confirmPassword) {
                    binding.confirmPasswordEditText.error = getString(R.string.error_message_password_doesnt_match)
                    binding.confirmPasswordEditText.requestFocus()
                    return
                }
                binding.passwordEditText.hideKeyboard(this)
                viewModel.changePassword(emailId, password)
            }
        }
    }

    private fun setupObservers() {
        viewModel.changePasswordApiResponse.observe(this) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.changePasswordButton.isEnabled = false
                }
                is ApiResult.Error -> {
                    binding.changePasswordButton.isEnabled = true
                    showToast(it.errorMessage)

                }
                is ApiResult.Success -> {
                    binding.changePasswordButton.isEnabled = true
                    showToast(getString(R.string.success_message_password_change))
                    Handler(Looper.getMainLooper()).postDelayed({
                        finishAffinity()
                        navigateToActivity(UserAuthActivity::class.java)
                    }, 1000)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

}