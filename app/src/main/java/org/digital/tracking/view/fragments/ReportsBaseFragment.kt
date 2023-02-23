package org.digital.tracking.view.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import android.widget.EditText
import java.io.File
import java.io.IOException
import java.util.*

open class ReportsBaseFragment : BaseFragment() {

    private val FILE_PICKER_REQUEST = 987899
    private val savedFile: File? = null

    fun showDatePicker(editText: EditText) {
        var mYear: Int
        var mMonth: Int
        var mDay: Int
        var mHour: Int
        var mMinute: Int
        // Get Current Date
        context?.let {
            val calendar = Calendar.getInstance()
            mYear = calendar[Calendar.YEAR]
            mMonth = calendar[Calendar.MONTH]
            mDay = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                it, { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val localMonthOfYear = monthOfYear + 1
                    val selectedMonth: String = if (localMonthOfYear < 10) {
                        "0$localMonthOfYear" //if month less than 10 then ad 0 before month
                    } else {
                        localMonthOfYear.toString()
                    }
                    val selectedDay: String = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        dayOfMonth.toString()
                    } //local variable
                    val selectedDateString = "$selectedDay-$selectedMonth-$year"
//                selectedDate = selectedDateString
                    editText.setText(selectedDateString)
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    fun exportReports(jsonString: String) {

    }


    // use system file picker Intent to select destination directory
    private fun selectExternalStorageFolder(fileName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        startActivityForResult(intent, FILE_PICKER_REQUEST)
    }

    // receive Uri for selected directory
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_PICKER_REQUEST -> data?.data?.let { destinationUri ->
                copyFileToExternalStorage(requireContext(), destinationUri)
            }
        }
    }

    // use ContentResolver to write file by Uri
    private fun copyFileToExternalStorage(context: Context, destination: Uri) {
        try {
            val outputStream = context.contentResolver.openOutputStream(destination) ?: return
            outputStream.write(savedFile?.readBytes())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}