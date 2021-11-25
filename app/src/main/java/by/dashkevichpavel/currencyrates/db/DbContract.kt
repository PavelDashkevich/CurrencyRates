package by.dashkevichpavel.currencyrates.db

import android.provider.BaseColumns

object DbContract {
    const val DATABASE_NAME = "currency_rates.db"

    object Currencies {
        const val TABLE_NAME = "currencies"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_CODE = "code"
        const val COLUMN_NAME_ABBREVIATION = "abbreviation"
        const val COLUMN_NAME_QUOT_NAME = "quot_name"
        const val COLUMN_NAME_ORDER_IN_LIST = "order_in_list"
        const val COLUMN_NAME_SHOW = "show"
    }
}