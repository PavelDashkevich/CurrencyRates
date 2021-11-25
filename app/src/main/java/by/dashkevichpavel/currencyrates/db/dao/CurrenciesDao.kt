package by.dashkevichpavel.currencyrates.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.dashkevichpavel.currencyrates.db.DbContract
import by.dashkevichpavel.currencyrates.db.entity.CurrencyEntity

@Dao
interface CurrenciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyEntity: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyEntities: List<CurrencyEntity>)

    @Query("""SELECT * FROM ${DbContract.Currencies.TABLE_NAME}""")
    suspend fun getAll(): List<CurrencyEntity>
}