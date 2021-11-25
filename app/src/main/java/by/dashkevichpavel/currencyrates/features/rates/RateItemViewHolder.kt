package by.dashkevichpavel.currencyrates.features.rates

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.dashkevichpavel.currencyrates.databinding.ListItemRateBinding
import by.dashkevichpavel.currencyrates.model.Rate
import by.dashkevichpavel.currencyrates.utils.formatters.FormatUtil

class RateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemRateBinding.bind(itemView)

    fun bind(rateItem: RateItem) {
        binding.tvCurrencyShortName.text = rateItem.currency.abbreviation
        binding.tvCurrencyFullName.text = rateItem.currency.quotName
        binding.tvRateFirst.text = getRateFormatted(rateItem.rateFirst)
        binding.tvRateLast.text = getRateFormatted(rateItem.rateLast)
    }

    private fun getRateFormatted(rate: Rate?): String {
        var result = ""

        rate?.let {
            result = FormatUtil.formatNumberAsRate(rate.rate)
        }

        return result
    }
}