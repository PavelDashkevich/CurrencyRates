package by.dashkevichpavel.currencyrates.network

import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate
import java.util.*

interface CurrenciesRatesService {
    suspend fun getCurrencies(): List<Currency>
    suspend fun getRates(onDate: Date): List<Rate>
}