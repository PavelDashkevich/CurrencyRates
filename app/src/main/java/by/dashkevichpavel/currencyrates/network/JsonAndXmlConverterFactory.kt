package by.dashkevichpavel.currencyrates.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class JsonAndXmlConverterFactory(
    private val jsonFactory: Converter.Factory,
    private val xmlFactory: Converter.Factory
    ) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            return when (annotation) {
                is JsonFormat ->
                    jsonFactory.responseBodyConverter(type, annotations, retrofit)
                is XmlFormat ->
                    xmlFactory.responseBodyConverter(type, annotations, retrofit)
                else -> null
            }
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        for (annotation in parameterAnnotations) {
            when (annotation) {
                is JsonFormat -> return jsonFactory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
                )
                is XmlFormat -> return xmlFactory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
                )
                else -> null
            }
        }
        return null
    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class JsonFormat

@Retention(AnnotationRetention.RUNTIME)
annotation class XmlFormat