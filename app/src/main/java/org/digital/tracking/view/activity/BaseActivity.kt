package org.digital.tracking.view.activity

import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.digital.tracking.R
import org.digital.tracking.utils.makeGone
import org.digital.tracking.utils.makeVisible
import org.digital.tracking.utils.setDrawable
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    fun initToolbar(view: View, title: String) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val leftIcon = view.findViewById<ImageView>(R.id.left_action)
        val rightIcon = view.findViewById<ImageView>(R.id.right_action)
        toolbarTitle.text = title
        leftIcon.setOnClickListener {
            onBackPressed()
        }
    }

    fun initToolbar(view: View, title: String, rightIconVisibility: Boolean = false, rightIconClickListener: View.OnClickListener? = null) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_title)
        val leftIcon = view.findViewById<ImageView>(R.id.left_action)
        val rightIcon = view.findViewById<ImageView>(R.id.right_action)
        toolbarTitle.text = title
        leftIcon.setOnClickListener {
            onBackPressed()
        }
        if (rightIconVisibility) {
            rightIcon.makeVisible()
        }
        rightIcon.setOnClickListener(rightIconClickListener)
        when (title) {
            getString(R.string.title_history_view),
            getString(R.string.title_replay_view),
            getString(R.string.title_advance_history) -> {
                rightIcon.setDrawable(this, R.drawable.ic_baseline_filter_list_24)
            }
            else -> {
                rightIcon.makeGone()
            }
        }
    }

    fun showDatePicker(editText: EditText) {
        var mYear: Int
        var mMonth: Int
        var mDay: Int
        var mHour: Int
        var mMinute: Int
        // Get Current Date
        val calendar = Calendar.getInstance()
        mYear = calendar[Calendar.YEAR]
        mMonth = calendar[Calendar.MONTH]
        mDay = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this, { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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