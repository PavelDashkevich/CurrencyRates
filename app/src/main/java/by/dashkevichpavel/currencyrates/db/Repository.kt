package by.dashkevichpavel.currencyrates.db

import by.dashkevichpavel.currencyrates.model.Currency

interface Repository {
    suspend fun insertCurrencies(currencies: List<Currency>)
    suspend fun getAllCurrencies(): List<Currency>
}