package by.dashkevichpavel.currencyrates

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.dashkevichpavel.currencyrates.db.RepositoryImpl
import by.dashkevichpavel.currencyrates.features.rates.RatesViewModel
import by.dashkevichpavel.currencyrates.network.CurrenciesRatesService
import by.dashkevichpavel.currencyrates.network.CurrenciesRatesServiceImpl

class CurrencyRatesViewModelFactory(
    private val applicationContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            RatesViewModel::class.java ->
                RatesViewModel(
                    RepositoryImpl(applicationContext),
                    CurrenciesRatesServiceImpl()
                )
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T
}