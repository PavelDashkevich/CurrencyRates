package by.dashkevichpavel.currencyrates.utils.mappers

import by.dashkevichpavel.currencyrates.db.entity.CurrencyEntity
import by.dashkevichpavel.currencyrates.model.Currency
import by.dashkevichpavel.currencyrates.model.Rate
import by.dashkevichpavel.currencyrates.network.CurrencyXml
import by.dashkevichpavel.currencyrates.network.CurrencyJson
import by.dashkevichpavel.currencyrates.network.DailyExRatesXml
import by.dashkevichpavel.currencyrates.network.RateJson
import by.dashkevichpavel.currencyrates.utils.datetime.DateTimeUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun CurrencyJson.mapToCurrency(): Currency {
    var dateEnd: Date = DateTimeUtil.getTomorrow()

    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    try {
        dateEnd = format.parse(this.dateEnd) ?: DateTimeUtil.getTomorrow()
    } catch(e: ParseException) {
        e.printStackTrace()
    }

    return Currency(
        id = this.id,
        code = this.code,
        abbreviation = this.abbreviation,
        quotName = this.quotName,
        dateEnd = dateEnd
    )
}

fun List<CurrencyJson>.mapToCurrencies(): List<Currency> =
    this.map { currencyJson -> currencyJson.mapToCurrency() }

fun RateJson.mapToRate(): Rate = Rate(currencyId = this.id, rate = this.officialRate)

fun List<RateJson>.mapToRates(): List<Rate> = this.map { rateJson -> rateJson.mapToRate() }

fun CurrencyXml.mapToRate(): Rate =
    Rate(currencyId = this.id, rate = this.rate)

fun DailyExRatesXml.mapToRates(): List<Rate> =
    this.list.map { currencyXml -> currencyXml.mapToRate() }

fun Currency.mapToCurrencyEntity(): CurrencyEntity =
    CurrencyEntity(
        id = this.id,
        code = this.code,
        abbreviation = this.abbreviation,
        quotName = this.quotName,
        order = this.order,
        show = this.show
    )

fun List<Currency>.mapToCurrencyEntities(): List<CurrencyEntity> =
    this.map { currency -> currency.mapToCurrencyEntity() }

fun CurrencyEntity.mapToCurrency(): Currency =
    Currency(id = this.id,
        code = this.code,
        abbreviation = this.abbreviation,
        quotName = this.quotName,
        order = this.order,
        show = this.show
    )

@JvmName("mapToCurrenciesCurrencyEntity")
fun List<CurrencyEntity>.mapToCurrencies(): List<Currency> =
    this.map { currencyEntity -> currencyEntity.mapToCurrency() }