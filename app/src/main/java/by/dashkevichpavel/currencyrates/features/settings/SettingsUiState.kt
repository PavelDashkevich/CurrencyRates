package by.dashkevichpavel.currencyrates.features.settings

sealed class SettingsUiState {
    object Start : SettingsUiState()
    object Loading : SettingsUiState()
    object Ready : SettingsUiState()
    object Saving : SettingsUiState()
    object SavingComplete: SettingsUiState()
}
