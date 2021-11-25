package by.dashkevichpavel.currencyrates.features.rates

import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate

data class RateItem(
    val currency: Currency,
    val rateFirst: Rate?,
    val rateLast: Rate?
)