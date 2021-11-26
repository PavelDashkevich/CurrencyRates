package by.dashkevichpavel.currencyrates.features.settings

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import androidx.core.view.accessibility.AccessibilityEventCompat.getAction
import androidx.recyclerview.widget.RecyclerView
import by.dashkevichpavel.currencyrates.databinding.ListItemCurrencySettingBinding

class CurrencySettingsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ListItemCurrencySettingBinding.bind(itemView)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        currencySettingsItem: CurrencySettingsItem,
        currencySettingsItemActionListener: CurrencySettingsItemActionListener
        ) {
        Log.d("CurrencyRatesApp", "bind currencySettingsItem: $currencySettingsItem")

        binding.tvCurrencyShortName.text = currencySettingsItem.currency.abbreviation
        binding.tvCurrencyFullName.text = currencySettingsItem.currency.quotName
        binding.swShow.isChecked = currencySettingsItem.currency.show

        binding.swShow.setOnClickListener {
            currencySettingsItemActionListener.onCurrencySettingsItemAction(
                CurrencySettingsItemAction.ChangeShowFlag(
                    currencySettingsItem.currency.id,
                    binding.swShow.isChecked
                )
            )
        }

        binding.iDrag.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN ||
                motionEvent.action == MotionEvent.ACTION_UP) {
                currencySettingsItemActionListener.onCurrencySettingsItemAction(
                    CurrencySettingsItemAction.StartDrag(this)
                )
            }
            false
        }
    }
}