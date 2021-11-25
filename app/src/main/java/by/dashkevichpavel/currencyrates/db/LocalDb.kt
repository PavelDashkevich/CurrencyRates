package by.dashkevichpavel.currencyrates.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.dashkevichpavel.currencyrates.db.dao.CurrenciesDao
import by.dashkevichpavel.currencyrates.db.entity.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = true
)
abstract class LocalDb : RoomDatabase() {
    abstract val currenciesDao: CurrenciesDao

    companion object {
        private var instance: LocalDb? = null

        fun getInstance(applicationContext: Context): LocalDb {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    LocalDb::class.java,
                    DbContract.DATABASE_NAME
                )
                    .build()
            }

            return instance as LocalDb
        }
    }
}