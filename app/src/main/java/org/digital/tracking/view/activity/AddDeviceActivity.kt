package org.digital.tracking.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityAddDeviceBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.utils.makeGone
import org.digital.tracking.utils.makeVisible
import org.digital.tracking.utils.showToast
import org.digital.tracking.viewModel.AddDeviceViewModel

@AndroidEntryPoint
class AddDeviceActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddDeviceBinding
    private val viewModel by viewModels<AddDeviceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.root, getString(R.string.add_new_device))

        setupObservers()

        binding.addDeviceButton.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.addDeviceButton.id -> {
                addNewDevice()
            }
        }
    }

    private fun addNewDevice() {
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

        val vehicleNumber = binding.vehicleNumberEditText.text.toString().trim()
        if (vehicleNumber.isEmpty()) {
            binding.vehicleNumberEditText.error = getString(R.string.error_message_invalid_vehicle_registration_number)
            binding.vehicleNumberEditText.requestFocus()
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

        val selectedDevicePosition = binding.selectDeviceSpinner.selectedItemPosition
        if (selectedDevicePosition == 0) {
            showToast(getString(R.string.message_error_invalid_device_type))
            binding.selectDeviceSpinner.requestFocus()
            return
        }

        val selectedDevice = "Other"

        viewModel.addDevice(imei, simNumber, vehicleType, vehicleNumber, selectedDevice)
    }

    private fun setupObservers() {
        viewModel.addDeviceApiResult.observe(this) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.addDeviceButton.isEnabled = false
                }
                is ApiResult.Error -> {
                    binding.addDeviceButton.isEnabled = true
                    showToast(it.errorMessage)
                }
                is ApiResult.Success -> {
                    val isDeviceAdded = it.data
                    if (isDeviceAdded) {
                        showToast(R.string.success_message_failed_to_add_new_device)
                        finish()
                    } else {
                        showToast(R.string.error_message_failed_to_add_new_device)
                    }
                }
            }
        }
    }

}