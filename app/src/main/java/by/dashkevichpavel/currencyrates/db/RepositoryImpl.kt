package by.dashkevichpavel.currencyrates.db

import android.content.Context
import by.dashkevichpavel.currencyrates.utils.mappers.mapToCurrencies
import by.dashkevichpavel.currencyrates.utils.mappers.mapToCurrencyEntities
import by.dashkevichpavel.currencyrates.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(applicationContext: Context) : Repository {
    private val localDb = LocalDb.getInstance(applicationContext)

    override suspend fun insertCurrencies(currencies: List<Currency>) =
        withContext(Dispatchers.IO) {
            localDb.currenciesDao.insert(currencies.mapToCurrencyEntities())
        }

    override suspend fun getAllCurrencies(): List<Currency> =
        withContext(Dispatchers.IO) {
            localDb.currenciesDao.getAll().mapToCurrencies()
        }
}