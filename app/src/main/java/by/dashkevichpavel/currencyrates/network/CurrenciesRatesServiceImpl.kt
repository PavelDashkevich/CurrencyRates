package by.dashkevichpavel.currencyrates.network

import android.util.Log
import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate
import by.dashkevichpavel.currencyrates.utils.formatters.FormatUtil
import by.dashkevichpavel.currencyrates.utils.mappers.mapToCurrencies
import by.dashkevichpavel.currencyrates.utils.mappers.mapToRates
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.create
import java.util.*

class CurrenciesRatesServiceImpl : CurrenciesRatesService {
    private val apiInstance = getServiceInstance()

    override suspend fun getCurrencies(): List<Currency> {
        var currencies: List<Currency> = emptyList()
        try {
            currencies = apiInstance?.getCurrencies()?.mapToCurrencies() ?: emptyList()
        } catch (e: Exception) {
            Log.d("CurrencyRatesApp", "Error in currencies loading:")
            e.printStackTrace()
        }

        return currencies
    }


    // can be used getRatesFromJson or getRatesFromXml
    // getRatesFromJson is a way recommended by NBRB
    override suspend fun getRates(onDate: Date): List<Rate> = getRatesFromXml(onDate)

    private suspend fun getRatesFromXml(onDate: Date): List<Rate> {
        var rates: List<Rate> = emptyList()
        try {
            rates = apiInstance?.getRatesXml(FormatUtil.formatDateAsMonthDayYearString(onDate))
                ?.mapToRates() ?: emptyList()
        } catch (e: Exception) {
            Log.d("CurrencyRatesApp", "Error in rates (xml) loading:")
            e.printStackTrace()
        }

        return rates
    }

    private suspend fun getRatesFromJson(onDate: Date): List<Rate> {
        var rates: List<Rate> = emptyList()
        try {
            rates = apiInstance?.getRatesJson(FormatUtil.formatDateAsYearMonthDayString(onDate))
                ?.mapToRates() ?: emptyList()
        } catch (e: Exception) {
            Log.d("CurrencyRatesApp", "Error in rates (xml) loading:")
            e.printStackTrace()
        }

        return rates
    }


    companion object {
        private var apiService: NbrbApiService? = null

        fun getServiceInstance(): NbrbApiService? {
            if (apiService == null) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val moshi: Moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(NbrbApiService.BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            //.addInterceptor(loggingInterceptor)
                            .build()
                    )
                    .addConverterFactory(
                        JsonAndXmlConverterFactory(
                            MoshiConverterFactory.create(moshi).asLenient(),
                            SimpleXmlConverterFactory.createNonStrict()
                        )
                    )
                    .build()

                apiService = retrofit.create()
            }

            return apiService
        }
    }
}