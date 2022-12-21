package org.digital.tracking.utils

import java.text.SimpleDateFormat
import java.util.*


fun getTodayDate(isStartDate: Boolean = false): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
//    calendar[Calendar.DATE] = calendar.getMinimum(Calendar.DAY_OF_MONTH)
//    calendar.add(Calendar.DATE, -1)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    if (isStartDate) {
        return "${date}T00:00:00.000Z"
    }
    return "${date}T${time}.000Z"
}

fun getYesterdayDate(isStartDate: Boolean = true): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -1)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    if (isStartDate) {
        return "${date}T00:00:00.000Z"
    }
    return "${date}T${time}.000Z"
}


fun getMonthStartDate(isStartDate: Boolean = true): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar[Calendar.DATE] = calendar.getMinimum(Calendar.DAY_OF_MONTH)
    calendar.add(Calendar.DATE, -1)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    if (isStartDate) {
        return "${date}T00:00:00.000Z"
    }
    return "${date}T${time}.000Z"
}

fun get7DaysTimestamp(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -6)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    return "${date}T${time}.000Z"
}

fun get15DaysTimestamp(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -14)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    return "${date}T${time}.000Z"
}

fun get30DaysTimestamp(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -29)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    return "${date}T${time}.000Z"
}

fun get60DaysTimestamp(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -59)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    return "${date}T${time}.000Z"
}

fun getMonthEndDate(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar[Calendar.DATE] = calendar.getMaximum(Calendar.DAY_OF_MONTH)
    val endDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(endDate)
    val time: String = timeFormat.format(endDate)
    return "${date}T${time}.000Z"
}

fun getYearStartDate(): String {
//    2021-04-30T18:30:00.000Z
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DATE, 1)
    calendar.set(Calendar.MONTH, 0)
    val startDate = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val date: String = dateFormat.format(startDate)
    val time: String = timeFormat.format(startDate)
    return "${date}T${time}.000Z"
}

//fun getReadableDateAndTime(date: String?, time: String?): String {
//    var dateTimeString = "$date $time"
//    var readableDateString = ""
//    var readableTimeString = ""
//    if (date.isNullOrEmpty() || time.isNullOrEmpty()) {
//        return ""
//    }
//    try {
//        //Format Date
//        val DATE_OLD_FORMAT = "yyyy-MM-dd"
//        val DATE_NEW_FORMAT = "dd MMM yyyy"
//        val sdf = SimpleDateFormat(DATE_OLD_FORMAT, Locale.getDefault())
//        val d = sdf.parse(date)
//        sdf.applyPattern(DATE_NEW_FORMAT)
//        readableDateString = sdf.format(d)
//        readableTimeString = time
//
//    } catch (exception: Exception) {
//        return dateTimeString
//    }
//    dateTimeString = "$readableDateString $readableTimeString"
//    return dateTimeString
//}

fun getReadableDateAndTime(date: String?, time: String?): String {
    var dateTimeString = "$date $time"
    if (date.isNullOrEmpty() || time.isNullOrEmpty()) {
        return ""
    }
    try {
        //Format Date
        val DATE_OLD_FORMAT = "yyyy-MM-dd"
        val DATE_NEW_FORMAT = "dd MMM yyyy"
        val formatter = SimpleDateFormat("$DATE_OLD_FORMAT HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse("$date $time")
        val localTimeZone = Calendar.getInstance().timeZone
        val dateFormatter = SimpleDateFormat("$DATE_NEW_FORMAT HH:mm:ss", Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = localTimeZone
        dateTimeString = dateFormatter.format(value)
    } catch (exception: Exception) {
        return dateTimeString
    }
    return dateTimeString
}

fun getReadableDate(date: String?, time: String?): String {
    var dateTimeString = "$date"
    if (date.isNullOrEmpty() || time.isNullOrEmpty()) {
        return ""
    }
    try {
        //Format Date
        val DATE_OLD_FORMAT = "yyyy-MM-dd"
        val DATE_NEW_FORMAT = "dd MMM yyyy"
        val formatter = SimpleDateFormat("$DATE_OLD_FORMAT HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse("$date $time")
        val localTimeZone = Calendar.getInstance().timeZone
        val dateFormatter = SimpleDateFormat(DATE_NEW_FORMAT, Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = localTimeZone
        dateTimeString = dateFormatter.format(value)
    } catch (exception: Exception) {
        return dateTimeString
    }
    return dateTimeString
}

fun getReadableTime(date: String?, time: String?): String {
    var dateTimeString = "$time"
    if (date.isNullOrEmpty() || time.isNullOrEmpty()) {
        return ""
    }
    try {
        //Format Date
        val DATE_OLD_FORMAT = "yyyy-MM-dd"
        val DATE_NEW_FORMAT = "dd MMM yyyy"
        val formatter = SimpleDateFormat("$DATE_OLD_FORMAT HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse("$date $time")
        val localTimeZone = Calendar.getInstance().timeZone
        val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = localTimeZone
        dateTimeString = dateFormatter.format(value)
    } catch (exception: Exception) {
        return dateTimeString
    }
    return dateTimeString
}


fun String?.getReadableDate(): String {
    if (this == null) {
        return emptyString
    }
    return try {
        val OLD_FORMAT = "yyyy-MM-dd"
        val DATE_NEW_FORMAT = "dd MMM yyyy"
        val readableDateString: String
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse(this)
        val localTimeZone = Calendar.getInstance().timeZone
        val dateFormatter = SimpleDateFormat(DATE_NEW_FORMAT, Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = localTimeZone
        readableDateString = dateFormatter.format(value)
        readableDateString
    } catch (exception: Exception) {
        this
    }
}

//fun String?.formatDateForServer(isStartDate: Boolean = false): String {
//    if (this.isNullOrEmpty()) {
//        return emptyString
//    }
//    val dateSplitArray = this.split("-")
//    val dateString = "${dateSplitArray[2]}-${dateSplitArray[1]}-${dateSplitArray[0]}"
//    val calendarTime = Calendar.getInstance().time
//    val timeFormat = SimpleDateFormat("HH:mm:ss")
//    val timeString: String = timeFormat.format(calendarTime)
//    if (isStartDate) {
//        return "${dateString}T00:00:00.000Z"
//    }
//    return "${dateString}T${timeString}.000Z"
//}

//fun String?.formatDateForServer(isStartDate: Boolean = false): String {
//    if (this.isNullOrEmpty()) {
//        return emptyString
//    }
//    val dateSplitArray = this.split("-")
//    val dateString = "${dateSplitArray[2]}-${dateSplitArray[1]}-${dateSplitArray[0]}"
//    val calendarTime = Calendar.getInstance()
//    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//    timeFormat.timeZone = TimeZone.getTimeZone("UTC")
//    var timeString = "00:00:00"
//
//    timeString = if (isStartDate) {
//        calendarTime.set(Calendar.HOUR_OF_DAY, 0)
//        calendarTime.set(Calendar.MINUTE, 0)
//        calendarTime.set(Calendar.SECOND, 0)
//        timeFormat.format(calendarTime.time)
//    } else {
//        timeFormat.format(calendarTime.time)
//    }
//    return timeString
//}

fun String?.formatDateForServer(isStartDate: Boolean = false): String {
    if (this.isNullOrEmpty()) {
        return emptyString
    }
    val dateSplitArray = this.split("-")
    //"2022-12-05"
    //[0] -> 2022    year
    //[1] -> 12    month
    //[2] -> 05    day
    val dateString = "${dateSplitArray[2]}-${dateSplitArray[1]}-${dateSplitArray[0]}"
    val calendarTime = Calendar.getInstance()
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.00Z'")
    timeFormat.timeZone = TimeZone.getTimeZone("UTC")
    var timeString: String
    val year = dateSplitArray[2].toInt()
    val month = dateSplitArray[1].toInt()
    val day = dateSplitArray[0].toInt()
    calendarTime.set(Calendar.YEAR, year)
    calendarTime.set(Calendar.MONTH, month - 1)
    calendarTime.set(Calendar.DAY_OF_MONTH, day)
    timeString = if (isStartDate) {
        calendarTime.set(Calendar.HOUR_OF_DAY, 0)
        calendarTime.set(Calendar.MINUTE, 0)
        calendarTime.set(Calendar.SECOND, 0)
        timeFormat.format(calendarTime.time)
    } else {
        timeFormat.format(calendarTime.time)
    }
    return timeString
}


