package by.dashkevichpavel.currencyrates.features.rates

sealed class RatesUiState {
    object Start : RatesUiState()
    object Loading : RatesUiState()
    object LoadingError : RatesUiState()
    object NotLoaded : RatesUiState()
    object Ready : RatesUiState()
}
