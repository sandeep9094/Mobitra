package org.digital.tracking.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityLoginPhoneBinding
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.BaseActivity
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SignUpPhoneActivity : BaseActivity(), View.OnClickListener {

    private var verificationId = ""

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.sign_up_with_phone))

        //below line is for getting instance of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance()
        mAuth.useAppLanguage()

        binding.sendOtpButton.setOnClickListener(this)
        binding.verifyOtpButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.sendOtpButton.id -> {
                val phoneNumber = binding.phoneEditText.text.toString()
                if (phoneNumber.isEmpty() || phoneNumber.length < 10) {
                    binding.phoneEditText.error = getString(R.string.message_error_phone_number)
                    binding.phoneEditText.requestFocus()
                    return
                }
                binding.phoneEditText.hideKeyboard(this)
                sendOtp(phoneNumber)
            }
            binding.verifyOtpButton.id -> {
                val otp = binding.verifyOtpEditText.text.toString()
                if (otp.isEmpty() || otp.length < 6) {
                    binding.verifyOtpEditText.error = getString(R.string.message_error_valid_otp)
                    binding.verifyOtpEditText.requestFocus()
                    return
                }
                binding.verifyOtpEditText.hideKeyboard(this)
                verifyOtp(otp)
            }
        }
    }

    private fun sendOtp(number: String) {
        //this method is used for getting OTP on user phone number.
        val phoneNumber = "+91$number"
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.progressBar.makeVisible()
        binding.sendOtpLayout.makeGone()
        binding.verifyOtpLayout.makeGone()
    }

    //callback method is called on Phone auth provider.
    //initializing our callbacks for on verification callback method.
    private val mCallBack: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        //below method is used when OTP is sent from Firebase
        override fun onCodeSent(string: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(string, forceResendingToken)
            Timber.d("onCodeSent : $verificationId")
            //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
            verificationId = string

            binding.progressBar.makeGone()
            binding.sendOtpLayout.makeGone()
            binding.verifyOtpLayout.makeVisible()
        }

        //this method is called when user recieve OTP from Firebase.
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            Timber.d("onVerificationCompleted --------------")
            //below line is used for getting OTP code which is sent in phone auth credentials.
            val code = phoneAuthCredential.smsCode
            //checking if the code is null or not.
            code?.let {
                //if the code is not null then we are setting that code to our OTP edittext field.
                binding.verifyOtpEditText.setText(code)
                //after setting this code to OTP edittext field we are calling our verifycode method.
                verifyOtp(code)
            }
        }

        //thid method is called when firebase doesnot sends our OTP code due to any error or issue.
        override fun onVerificationFailed(e: FirebaseException) {
            Timber.d("onVerificationFailed : ${e.message}")
            //displaying error message with firebase exception.
            showToast(e.message.toString())
            binding.progressBar.makeGone()
            binding.sendOtpLayout.makeVisible()
            binding.verifyOtpLayout.makeGone()
        }
    }

    //below method is use to verify code from Firebase.
    private fun verifyOtp(code: String) {
        //below line is used for getting getting credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        //after getting credential we are calling sign in method.
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        //inside this method we are checking if the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(getString(R.string.message_success_sign_in))
                    val phoneNumber = binding.phoneEditText.text.toString()
                    signUpUser(phoneNumber)
                } else {
                    binding.verifyOtpEditText.error = getString(R.string.message_error_invalid_otp)
                }
            }
    }

    private fun signUpUser(phoneNumber: String) {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.apply {
            putExtra(Constants.INTENT_KEY_SIGN_UP_TYPE, SignUpType.PHONE)
            putExtra(Constants.INTENT_KEY_PHONE, phoneNumber)
        }
        navigateToActivity(intent)
    }
}