package com.planes.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.TimeZone

class DateTimeUtils {
    companion object{

        fun  getDateTimeNowAsString(): String {
            val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC");
            return formatter.format(Instant.now())
        }

        fun parseDate(formattedDate: String): Date? {
            val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC");
            return formatter.parse(formattedDate)
        }

        fun getStringFromDate(date: Date): String {
            val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC");
            return formatter.format(date)
        }
    }
}