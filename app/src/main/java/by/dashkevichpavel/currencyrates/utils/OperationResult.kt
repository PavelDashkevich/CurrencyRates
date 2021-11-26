package by.dashkevichpavel.currencyrates.utils

sealed class OperationResult<T>(val value: T?) {
    class Success<T>(value: T?) : OperationResult<T>(value)
    class Error<T>(val errorMessage: String = "") : OperationResult<T>(null)
}