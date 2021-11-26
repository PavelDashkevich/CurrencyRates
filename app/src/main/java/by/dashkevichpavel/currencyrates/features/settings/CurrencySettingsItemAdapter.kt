package by.dashkevichpavel.currencyrates.features.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.dashkevichpavel.currencyrates.R
import java.util.*

class CurrencySettingsItemAdapter(
    private val currencySettingsItemActionListener: CurrencySettingsItemActionListener
    ) : RecyclerView.Adapter<CurrencySettingsItemViewHolder>(), ItemTouchHelperAdapter {
    private val items: MutableList<CurrencySettingsItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencySettingsItemViewHolder = CurrencySettingsItemViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_currency_setting, parent, false)
    )

    override fun onBindViewHolder(holder: CurrencySettingsItemViewHolder, position: Int) {
        holder.bind(items[position], currencySettingsItemActionListener)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<CurrencySettingsItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.d("CurrencyRatesApp", "onItemMove")
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        currencySettingsItemActionListener.onCurrencySettingsItemAction(
            CurrencySettingsItemAction.MoveItem(fromPosition, toPosition)
        )
        return true
    }
}