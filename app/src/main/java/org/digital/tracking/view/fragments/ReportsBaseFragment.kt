package org.digital.tracking.view.fragments

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.EditText
import java.util.*

open class ReportsBaseFragment : BaseFragment() {

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

}