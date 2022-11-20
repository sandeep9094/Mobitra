package org.digital.tracking.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivitySignUpEmailBinding
import org.digital.tracking.utils.Constants
import org.digital.tracking.utils.hideKeyboard
import org.digital.tracking.utils.isEmailValid
import org.digital.tracking.utils.navigateToActivity
import org.digital.tracking.view.activity.BaseActivity

class SignUpEmailActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.sign_up_with_email))

        binding.createAccountButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.createAccountButton.id -> {
                createAccountWithEmail()
            }
        }
    }

    private fun createAccountWithEmail() {
        val userEmail = binding.emailEditText.text.toString()
        if (!userEmail.isEmailValid()) {
            binding.emailEditText.error = getString(R.string.error_message_invalid_email)
            binding.emailEditText.requestFocus()
            return
        }
        binding.emailEditText.hideKeyboard(this)
        signUpUser(userEmail)
    }

    private fun signUpUser(userEmail: String) {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.apply {
            putExtra(Constants.INTENT_KEY_SIGN_UP_TYPE, SignUpType.EMAIL)
            putExtra(Constants.INTENT_KEY_EMAIL, userEmail)
        }
        navigateToActivity(intent)
    }

}