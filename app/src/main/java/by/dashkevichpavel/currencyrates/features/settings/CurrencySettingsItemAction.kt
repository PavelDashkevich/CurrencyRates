package by.dashkevichpavel.currencyrates.features.settings

import androidx.recyclerview.widget.RecyclerView

sealed class CurrencySettingsItemAction {
    class StartDrag(val viewHolder: RecyclerView.ViewHolder) : CurrencySettingsItemAction()
    class ChangeShowFlag(val currencyId: Int, val isChecked: Boolean) : CurrencySettingsItemAction()
    class MoveItem(val fromPosition: Int, val toPosition: Int) : CurrencySettingsItemAction()
}

interface CurrencySettingsItemActionListener {
    fun onCurrencySettingsItemAction(action: CurrencySettingsItemAction)
}