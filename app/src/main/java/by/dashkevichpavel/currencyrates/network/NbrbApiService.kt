package by.dashkevichpavel.currencyrates.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface NbrbApiService {
    @GET("api/exrates/currencies")
    @JsonFormat
    suspend fun getCurrencies(): List<CurrencyJson>

    @GET("api/exrates/rates?periodicity=0")
    @JsonFormat
    suspend fun getRatesJson(@Query("onDate") onDate: String): List<RateJson>

    @GET("Services/XmlExRates.aspx")
    @XmlFormat
    suspend fun getRatesXml(@Query("onDate") onDate: String): DailyExRatesXml

    companion object {
        const val BASE_URL = "https://www.nbrb.by/"
    }
}

@JsonClass(generateAdapter = true)
data class CurrencyJson(
    @Json(name = "Cur_ID")           val id: Int,
    @Json(name = "Cur_Code")         val code: String,
    @Json(name = "Cur_Abbreviation") val abbreviation: String,
    @Json(name = "Cur_QuotName")     val quotName: String,
    @Json(name = "Cur_DateEnd")      val dateEnd: String
)

@JsonClass(generateAdapter = true)
data class RateJson(
    @Json(name = "Cur_ID")           val id: Int,
    @Json(name = "Cur_OfficialRate") val officialRate: Double
)

@Root(name = "DailyExRates", strict = false)
data class DailyExRatesXml(
    @field:Attribute(name = "Date", required = false)
    var date: String = "",

    @field:ElementList(inline = true, entry = "Currency", required = false)
    var list: MutableList<CurrencyXml> = mutableListOf()
)

@Root(name = "Currency", strict = true)
data class CurrencyXml(
    @field:Attribute(name = "Id")     var id: Int = 0,
    @field:Element(name = "CharCode") var charCode: String = "",
    @field:Element(name = "Scale")    var scale: Int = 0,
    @field:Element(name = "Name")     var name: String = "",
    @field:Element(name = "NumCode")  var numCode: String = "",
    @field:Element(name = "Rate")     var rate: Double = 0.0
)
