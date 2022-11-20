package org.digital.tracking.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.api.ApiService
import org.digital.tracking.databinding.ActivitySignUpBinding
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.*
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.BaseActivity
import org.digital.tracking.view.activity.MainActivity
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener {

//    private var deviceList: List<DeviceListItem> = ArrayList()

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.create_account))

        prefillSignUpData()
//        fetchDevices()
        binding.createAccountButton.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.createAccountButton.id -> {
                signUpUser()
            }
        }
    }

    private fun prefillSignUpData() {
        val signUpType = intent.extras?.getString(Constants.INTENT_KEY_SIGN_UP_TYPE, "")
        when (signUpType) {
            SignUpType.PHONE -> {
                val phoneNumber = intent.extras?.getString(Constants.INTENT_KEY_PHONE, "")
                binding.phoneEditText.setText(phoneNumber)
            }
            SignUpType.EMAIL -> {
                val userEmail = intent.extras?.getString(Constants.INTENT_KEY_EMAIL, "")
                binding.emailEditText.setText(userEmail)
            }
        }
    }

    private fun fetchDevices() {
//        viewModel.getDeviceList()
//        viewModel.deviceListResponse.observe(this) { response ->
//            response?.let {
//                when (it) {
//                    is ResponseResult.InProgress -> {
//                    }
//                    is ResponseResult.Success -> {
//                        deviceList = it.data
//                        updateSpinnerAdapter(deviceList)
//                    }
//                    is ResponseResult.Error -> {
//                    }
//                }
//            }
//        }
    }

    private fun updateSpinnerAdapter(list: List<DeviceListItem>) {
        val deviceList = ArrayList<String>()
        deviceList.add(getString(R.string.select_device))
        list.forEach { device ->
            deviceList.add(device.name)
        }

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, deviceList
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectDeviceSpinner.adapter = arrayAdapter
    }

    private fun signUpUser() {
        val imei = binding.imeiEditText.text.toString().trim()
        if (imei.isEmpty()) {
            binding.imeiEditText.error = getString(R.string.message_error_invalid_imei)
            binding.imeiEditText.requestFocus()
            return
        }

        val simNumber = binding.simNumberEditText.text.toString().trim()
        if (simNumber.isEmpty()) {
            binding.simNumberEditText.error = getString(R.string.message_error_invalid_sim_number)
            binding.simNumberEditText.requestFocus()
            return
        }

        val selectedVehiclePosition = binding.vehicleTypeSpinner.selectedItemPosition
        val vehicleTypeList = resources.getStringArray(R.array.spinner_vehicle_types_items)
        val vehicleType = vehicleTypeList[selectedVehiclePosition]
        if (vehicleType == getString(R.string.vehicle_type)) {
            showToast(getString(R.string.message_error_invalid_vehicle_type))
            binding.vehicleTypeSpinner.requestFocus()
            return
        }

        //TODO replace with select device type api
        val selectedDevicePosition = binding.selectDeviceSpinner.selectedItemPosition
//        val deviceTypeList = deviceList
        if (selectedDevicePosition == 0) {
            showToast(getString(R.string.message_error_invalid_device_type))
            binding.selectDeviceSpinner.requestFocus()
            return
        }
//        val selectedDevice = deviceTypeList[selectedDevicePosition - 1]

        val userTypePosition = binding.userTypeSpinner.selectedItemPosition
        val userTypeList = resources.getStringArray(R.array.spinner_user_type_items)
        val userType = userTypeList[userTypePosition]
        //TODO add replace userType as admin or non-admin

        val name = binding.nameEditText.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameEditText.error = getString(R.string.error_message_empty_name)
            binding.nameEditText.requestFocus()
            return
        }
        val phoneNumber = binding.phoneEditText.text.toString()
        if (phoneNumber.isEmpty() || phoneNumber.length < 10) {
            binding.phoneEditText.error = getString(R.string.message_error_phone_number)
            binding.phoneEditText.requestFocus()
            return
        }

        val countryPosition = binding.countrySpinner.selectedItemPosition
        val countryList = resources.getStringArray(R.array.spinner_select_country)
        val selectedCountry = countryList[countryPosition].toString()
        if (selectedCountry == getString(R.string.select_country)) {
            showToast(getString(R.string.message_error_invalid_country))
            binding.countrySpinner.requestFocus()
            return
        }

        val statePosition = binding.stateSpinner.selectedItemPosition
        val stateList = resources.getStringArray(R.array.spinner_select_state)
        val selectedState = stateList[statePosition].toString()
        if (selectedState == getString(R.string.select_state)) {
            showToast(getString(R.string.message_error_invalid_state))
            binding.stateSpinner.requestFocus()
            return
        }

        val city = binding.cityEditText.text.toString().trim()
        if (city.isEmpty()) {
            binding.cityEditText.error = getString(R.string.message_error_invalid_city)
            binding.cityEditText.requestFocus()
            return
        }

        val pinCode = binding.pinCodeEditText.text.toString().trim()
        if (pinCode.isEmpty() || pinCode.length < 6) {
            binding.pinCodeEditText.error = getString(R.string.message_error_invalid_pincode)
            binding.pinCodeEditText.requestFocus()
            return
        }

        val email = binding.emailEditText.text.toString().trim()
        if (!email.isEmailValid()) {
            binding.emailEditText.error = getString(R.string.error_message_invalid_email)
            binding.emailEditText.requestFocus()
            return
        }

        val address = binding.addressEditText.text.toString().trim()
        if (address.isEmpty()) {
            binding.addressEditText.error = getString(R.string.message_error_empty_address)
            binding.addressEditText.requestFocus()
            return
        }

        val userName = binding.userNameEditText.text.toString().trim()
        if (userName.isEmpty()) {
            binding.userNameEditText.error = getString(R.string.message_error_invalid_user_name)
            binding.userNameEditText.requestFocus()
            return
        }
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
        binding.confirmPasswordEditText.hideKeyboard(this)

        val deviceListItem = SignUpDeviceItem("Other", imei, "Mobile", simNumber, vehicleType)
        val credentials = Credentials(userName, password)
        val user = CreateUser(
            "", pinCode, selectedCountry, city, credentials, phoneNumber, name,
            "", "", address, listOf(deviceListItem), selectedState, "Admin", email
        )

        registerUser(user)
    }

    private fun registerUser(user: CreateUser) {
        viewModel.registerUser(user)
        viewModel.registerUserResponseResult.observe(this) { result ->
            result?.let {
                when (it) {
                    is ResponseResult.InProgress -> {
                        binding.inputScrollView.makeGone()
                        binding.progressBar.makeVisible()
                    }
                    is ResponseResult.Success -> {
                        binding.inputScrollView.makeGone()
                        binding.progressBar.makeGone()
                        if (it.data.status == "false") {
                            showToast(R.string.error_message_user_already_exist)
                            return@let
                        }
                        showToast(it.data.message)
                        openNextActivity()
                    }
                    is ResponseResult.Error -> {
                        showToast(it.errorMessage)
                        binding.inputScrollView.makeVisible()
                        binding.progressBar.makeGone()
                    }
                }
            }
        }
    }

    private fun openNextActivity() {
        sharedPrefs.isUserLoggedIn = false
        finishAffinity()
        startActivity(Intent(this, UserAuthActivity::class.java))
    }
}