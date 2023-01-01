package com.nmt.minhtu.doan.utils

import com.nmt.minhtu.doan.data_local.DataLocalManager
import java.text.*
import java.util.*

object Utils {
    fun isLogin(): Boolean {
        return DataLocalManager.getUser() != null
    }

    fun formatMoney(money: Int): String? {
        var result = ""
        try {
            val formatter: DecimalFormat = NumberFormat.getInstance(Locale.GERMANY) as DecimalFormat
            formatter.applyPattern("###,###,###")
            result = formatter.format(money).toString() + ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun convertMillisToDate(now: Long): String {
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        return formatter.format(calendar.time)
    }

    fun convertMillisToDate(now: Long, format: String): String {
        val formatter: DateFormat = SimpleDateFormat(format)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        return formatter.format(calendar.time)
    }

    fun getHourFromDate(time: String, format: String): Int {
        return try {
            val dateFormat = SimpleDateFormat(format)
            var date = dateFormat.parse(time)
            val calendar = Calendar.getInstance() // creates a new calendar instance
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY]
        } catch (e: ParseException) {
            -1
        }
    }
}