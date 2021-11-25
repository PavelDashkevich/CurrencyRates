package by.dashkevichpavel.currencyrates.features.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.dashkevichpavel.currencyrates.R

class RateItemsAdapter : RecyclerView.Adapter<RateItemViewHolder>() {
    private val items: MutableList<RateItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateItemViewHolder =
        RateItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_rate, parent, false)
        )

    override fun onBindViewHolder(holder: RateItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<RateItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}