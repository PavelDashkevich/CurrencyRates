package by.dashkevichpavel.currencyrates.features.rates

sealed class RatesViewModelState {
    object Start : RatesViewModelState()
    object Loading : RatesViewModelState()
    object LoadingError : RatesViewModelState()
    object NotLoaded : RatesViewModelState()
    object Ready : RatesViewModelState()
}
