package by.dashkevichpavel.currencyrates.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import by.dashkevichpavel.currencyrates.db.DbContract

@Entity(
    tableName = DbContract.Currencies.TABLE_NAME,
    indices = [Index(DbContract.Currencies.COLUMN_NAME_ID)]
)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_CODE)
    val code: String,

    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_ABBREVIATION)
    val abbreviation: String,

    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_QUOT_NAME)
    val quotName: String,

    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_ORDER_IN_LIST)
    val order: Int,

    @ColumnInfo(name = DbContract.Currencies.COLUMN_NAME_SHOW)
    val show: Boolean
)