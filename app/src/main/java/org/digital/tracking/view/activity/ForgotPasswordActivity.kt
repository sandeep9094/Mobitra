package org.digital.tracking.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityForgotPasswordBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.ForgotPasswordViewModel

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private var userName = ""
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.reset_password))

        binding.resetPasswordButton.setOnClickListener(this)

        setupObservers()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.resetPasswordButton.id -> {
                userName = binding.userNameEditText.text.toString()
                if (userName.isEmpty()) {
                    binding.userNameEditText.error = getString(R.string.error_message_invalid_username)
                    binding.userNameEditText.requestFocus()
                    return
                }
                binding.userNameEditText.hideKeyboard(this)
                viewModel.forgotPassword(userName)
            }
        }
    }

    private fun setupObservers() {
        viewModel.forgotPasswordApiResult.observe(this) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.resetPasswordButton.isEnabled = false
                }
                is ApiResult.Error -> {
                    binding.resetPasswordButton.isEnabled = true
                    showToast(it.errorMessage)
                }
                is ApiResult.Success -> {
                    binding.resetPasswordButton.isEnabled = true
                    showToast(getString(R.string.success_message_reset_password_send_otp))
                    openVerifyOTPScreen()
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    private fun openVerifyOTPScreen() {
        val intent = Intent(this, VerifyOtpActivity::class.java)
        intent.putExtra(Constants.INTENT_KEY_EMAIL, userName)
        navigateToActivity(intent)
    }

}