package by.dashkevichpavel.currencyrates.utils.datetime

import java.util.*

class DateTimeUtil {
    companion object {
        fun getToday(): Date = Date()

        fun getYesterday(): Date {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_MONTH, -1)
            return Date(c.timeInMillis)
        }

        fun getTomorrow(): Date {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_MONTH, 1)
            return Date(c.timeInMillis)
        }
    }
}