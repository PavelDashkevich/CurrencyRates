package by.dashkevichpavel.currencyrates.utils.formatters

import java.text.DecimalFormat
import java.util.*

class FormatUtil {
    companion object {
        fun formatDateAsMonthDayYearString(date: Date): String =
            android.text.format.DateFormat.format("MM.dd.yyyy", date).toString()

        fun formatDateAsYearMonthDayString(date: Date): String =
            android.text.format.DateFormat.format("yyyy-MM-dd", date).toString()

        fun formatDateAsDayMonthYearString(date: Date): String =
            android.text.format.DateFormat.format("dd.MM.yyyy", date).toString()

        fun formatNumberAsRate(number: Double): String =
            DecimalFormat("#.0000").format(number)
    }
}