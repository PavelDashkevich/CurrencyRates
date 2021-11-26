package by.dashkevichpavel.currencyrates.utils.extensions

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.dashkevichpavel.currencyrates.features.settings.CurrencySettingsItem
import by.dashkevichpavel.currencyrates.model.Currency
import java.util.*

fun Fragment.setupToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
}

fun Fragment.safelyNavigateTo(actionId: Int, args: Bundle? = null) {
    try {
        this.findNavController().navigate(actionId, args)
    } catch (e: IllegalArgumentException) {
        // fires when navigation action repeated before destination is opened,
        // i. e. user taps second time on the same control which must navigate user
        // to some destination
        Log.d("", "Navigation exception: ${e.message}")
    }
}

fun Currency.changeOrderWith(other: Currency) {
    val tmpOrder = this.order
    this.order = other.order
    other.order = tmpOrder
}

fun MutableList<CurrencySettingsItem>.changeItemsOrder(fromPosition: Int, toPosition: Int) {
    Collections.swap(this, fromPosition, toPosition)
}