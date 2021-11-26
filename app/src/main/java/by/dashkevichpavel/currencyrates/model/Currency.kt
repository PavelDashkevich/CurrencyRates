package by.dashkevichpavel.currencyrates.model

import java.util.*

data class Currency(
    val id: Int,
    val code: String,
    val abbreviation: String,
    val quotName: String,
    var order: Int = 0,
    var show: Boolean = false,
    val dateEnd: Date = Date()
)