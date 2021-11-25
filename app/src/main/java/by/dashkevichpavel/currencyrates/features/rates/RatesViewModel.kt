package by.dashkevichpavel.currencyrates.features.rates

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.dashkevichpavel.currencyrates.db.Repository
import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate
import by.dashkevichpavel.currencyrates.network.CurrenciesRatesService
import by.dashkevichpavel.currencyrates.utils.datetime.DateTimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class RatesViewModel(
    private val repository: Repository,
    private val currenciesRatesService: CurrenciesRatesService
) : ViewModel() {
    val state = MutableLiveData<RatesViewModelState>(RatesViewModelState.Start)
    val dateFirst = MutableLiveData(DateTimeUtil.getYesterday())
    val dateLast = MutableLiveData(DateTimeUtil.getToday())
    val rateItems = MutableLiveData<List<RateItem>>(emptyList())
    private var jobLoading: Job? = null
    private var currencies: MutableList<Currency> = mutableListOf()

    fun loadData() {
        if (state.value is RatesViewModelState.Start) {
            state.value = RatesViewModelState.Loading
            jobLoading = viewModelScope.launch(Dispatchers.IO) {

                when (val currenciesLoadingResult = checkAndLoadCurrencies()) {
                    is OperationResult.Error -> {
                        state.postValue(RatesViewModelState.LoadingError)
                        return@launch
                    }
                    is OperationResult.Success ->
                        currenciesLoadingResult.value?.let { newCurrenciesList ->
                            currencies = newCurrenciesList
                        }
                }

                when (val ratesLoadingResult = loadRates()) {
                    is OperationResult.Error -> {
                        state.postValue(RatesViewModelState.LoadingError)
                        return@launch
                    }
                    is OperationResult.Success ->
                        ratesLoadingResult.value?.let { newRateItems ->
                            rateItems.postValue(newRateItems)
                        }
                }

                state.postValue(RatesViewModelState.Ready)
            }
        }
    }

    fun errorHasShown() {
        state.value = RatesViewModelState.NotLoaded
    }

    private suspend fun checkAndLoadCurrencies(): OperationResult<MutableList<Currency>> {
        var currencies = repository.getAllCurrencies().toMutableList()

        if (currencies.isEmpty()) {
            currencies = currenciesRatesService.getCurrencies().toMutableList()

            if (currencies.isEmpty()) {
                return OperationResult.Error("Error of currencies loading.")
            }

            // delete old currencies
            val dateToday = DateTimeUtil.getToday()
            currencies = currencies.filter { currency ->
                currency.dateEnd.time >= dateToday.time
            }.toMutableList()

            // set default order - by id
            // set default visibility
            for (i in currencies.indices) {
                currencies[i] = currencies[i].copy(
                    order = currencies[i].id,
                    show = currencies[i].abbreviation in currenciesVisibleByDefault
                )
            }

            repository.insertCurrencies(currencies)
        }

        return OperationResult.Success(currencies)
    }

    private suspend fun loadRates(): OperationResult<List<RateItem>> {
        val tomorrow = DateTimeUtil.getTomorrow()
        val today = DateTimeUtil.getToday()
        val yesterday = DateTimeUtil.getYesterday()

        val rates = currenciesRatesService.getRates(tomorrow)
        val ratesToday = currenciesRatesService.getRates(today)

        val ratesFirst: List<Rate>
        val ratesLast: List<Rate>

        if (rates.isEmpty()) {
            ratesFirst = currenciesRatesService.getRates(DateTimeUtil.getYesterday())
            ratesLast = ratesToday

            dateFirst.postValue(yesterday)
            dateLast.postValue(today)
        } else {
            ratesFirst = ratesToday
            ratesLast = rates

            dateFirst.postValue(today)
            dateLast.postValue(tomorrow)
        }

        if (ratesFirst.isEmpty() && ratesLast.isEmpty()) {
            return OperationResult.Error("Rates are empty.")
        }

        var newItems = mutableListOf<RateItem>()

        currencies.forEach { currency: Currency ->
            newItems.add(
                RateItem(
                    currency,
                    ratesFirst.firstOrNull { rate -> rate.currencyId == currency.id },
                    ratesLast.firstOrNull { rate -> rate.currencyId == currency.id }
                )
            )
        }

        newItems.sortBy { rateItem -> rateItem.currency.order }
        newItems = newItems.filter { rateItem -> rateItem.currency.show }.toMutableList()

        return OperationResult.Success(newItems)
    }

    companion object {
        private val currenciesVisibleByDefault = setOf("USD", "EUR", "RUB")
    }
}