package com.domedav.namedays

import android.annotation.SuppressLint
import android.content.Context
import java.time.LocalDate
import java.time.temporal.ChronoField

class NamedaysHelper {
    companion object {
        private fun isLeapYear(year: Int): Boolean {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        }

        private fun getDayOfYear(date: LocalDate): Int {
            return date.get(ChronoField.DAY_OF_YEAR)
        }

        @SuppressLint("DiscouragedApi")
        fun getStringResource(context: Context): String {
            val today = LocalDate.now(java.time.ZoneId.systemDefault())
            val isLeapYear = isLeapYear(today.year)
            var dayOfYear = getDayOfYear(today)
            if (isLeapYear && dayOfYear > 55) {
                dayOfYear -= 1
            }
            if(isLeapYear && dayOfYear == 55) {
                return context.resources.getString(R.string.nameday_arr_item_leap)
            }
            return context.getString(context.resources.getIdentifier("nameday_arr_item_$dayOfYear", "string", context.packageName))
        }
    }
}