package com.movista.app.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd hh:mm"
const val TIME_FORMAT = "hh:mm"

object DateUtils {

    fun toDateString(date: Date?): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        return dateFormat.format(date)
    }

    fun toDateLongString(date: Date?): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        return dateFormat.format(date)
    }

    fun getDate(day: Int, month: Int, year: Int): Date {
        val calendar = GregorianCalendar(year, month, day)
        return calendar.time
    }

    fun toTimeString(date: Date?): String {
        val dateFormat = SimpleDateFormat(TIME_FORMAT)
        return dateFormat.format(date)
    }
}