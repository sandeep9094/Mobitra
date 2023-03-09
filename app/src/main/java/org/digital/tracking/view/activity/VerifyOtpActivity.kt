package org.digital.tracking.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityVerifyOtpBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.utils.Constants
import org.digital.tracking.utils.hideKeyboard
import org.digital.tracking.utils.navigateToActivity
import org.digital.tracking.utils.showToast
import org.digital.tracking.viewModel.ForgotPasswordViewModel

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivity(), View.OnClickListener {

    private var username = ""
    private lateinit var binding: ActivityVerifyOtpBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.one_time_password))

        username = intent.getStringExtra(Constants.INTENT_KEY_EMAIL) ?: ""

        binding.verifyOtpButton.setOnClickListener(this)

        setupObservers()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.verifyOtpButton.id -> {
                val otp = binding.otpEditText.text.toString()
                if (otp.length < 6) {
                    binding.otpEditText.error = getString(R.string.message_error_invalid_otp)
                    binding.otpEditText.requestFocus()
                    return
                }
                binding.otpEditText.hideKeyboard(this)
                viewModel.verifyOtp(username, otp)
            }

        }
    }

    private fun setupObservers() {
        viewModel.verifyOtpApiResponse.observe(this) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.verifyOtpButton.isEnabled = false
                }
                is ApiResult.Error -> {
                    binding.verifyOtpButton.isEnabled = true
                    showToast(it.errorMessage)
                }
                is ApiResult.Success -> {
                    binding.verifyOtpButton.isEnabled = true
                    openChangePasswordScreen()
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    private fun openChangePasswordScreen() {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        intent.putExtra(Constants.INTENT_KEY_EMAIL, username)
        navigateToActivity(intent)
    }


}