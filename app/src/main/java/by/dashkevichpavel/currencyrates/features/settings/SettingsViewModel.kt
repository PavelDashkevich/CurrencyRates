package by.dashkevichpavel.currencyrates.features.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.dashkevichpavel.currencyrates.db.Repository
import by.dashkevichpavel.currencyrates.utils.extensions.changeOrderWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    private var jobSave: Job? = null
    val currencySettingsItems = MutableLiveData<MutableList<CurrencySettingsItem>>(mutableListOf())
    val state = MutableLiveData<SettingsUiState>(SettingsUiState.Start)

    fun loadSettings() {
        if (state.value is SettingsUiState.Start) {
            viewModelScope.launch(Dispatchers.IO) {
                state.postValue(SettingsUiState.Loading)

                val currencies = repository.getAllCurrencies()
                currencySettingsItems.postValue(
                    currencies
                        .map { currency -> CurrencySettingsItem(currency) }
                        .sortedBy { currencySettingsItem -> currencySettingsItem.currency.order }
                        .toMutableList()
                )

                state.postValue(SettingsUiState.Ready)
            }
        }
    }

    fun changeItemShowFlag(currencyId: Int, isChecked: Boolean) {
        currencySettingsItems.value?.let { items ->
            items.firstOrNull { item -> item.currency.id == currencyId }?.currency?.show = isChecked
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        currencySettingsItems.value?.let { items ->
            Collections.swap(items, fromPosition, toPosition)
            items[fromPosition].currency.changeOrderWith(items[toPosition].currency)
        }
    }

    fun saveSettings() {
        if (jobSave == null || jobSave?.isCompleted == true) {
            jobSave = viewModelScope.launch(Dispatchers.IO) {
                state.postValue(SettingsUiState.Saving)

                currencySettingsItems.value?.let { items ->
                    repository.insertCurrencies(
                        items.map { currencySettingsItem: CurrencySettingsItem ->
                            currencySettingsItem.currency
                        }
                    )
                }

                state.postValue(SettingsUiState.SavingComplete)
            }
        }
    }

    fun notificationHasShown() {
        state.value = SettingsUiState.Ready
    }
}