package by.dashkevichpavel.currencyrates.network

import by.dashkevichpavel.currencyrates.utils.mappers.mapToCurrencies
import by.dashkevichpavel.currencyrates.utils.mappers.mapToRates
import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate
import by.dashkevichpavel.currencyrates.utils.formatters.FormatUtil
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

    override suspend fun getCurrencies(): List<Currency> =
        apiInstance?.getCurrencies()?.mapToCurrencies() ?: emptyList()

    override suspend fun getRates(onDate: Date): List<Rate> = getRatesFromXml(onDate)

    private suspend fun getRatesFromXml(onDate: Date): List<Rate> =
        apiInstance?.getRatesXml(FormatUtil.formatDateAsMonthDayYearString(onDate))?.mapToRates()
            ?: emptyList()

    private suspend fun getRatesFromJson(onDate: Date): List<Rate> =
        apiInstance?.getRatesJson(FormatUtil.formatDateAsYearMonthDayString(onDate))?.mapToRates()
            ?: emptyList()

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