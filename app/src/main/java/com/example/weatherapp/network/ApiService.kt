package com.example.weatherapp.network

import com.example.weatherapp.entity.ErrorMessageRoot
import com.example.weatherapp.entity.WeatherRoot
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://api.weatherstack.com/"

//Make changes here for putting your API key
private const val API_KEY = "Put your API key here"

private val moshiObj = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


interface WeatherApiServices {
    @GET("current")
    fun getWeatherMapAsync(@Query("query") city: String)
            : Deferred<WeatherRoot>

    @GET("current")
    fun getErrorMessageAsync(@Query("query") city: String)
            : Deferred<ErrorMessageRoot>
}

object WeatherApi {
    operator fun invoke(): WeatherApiServices {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("access_key", API_KEY)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshiObj))
            .build()
            .create(WeatherApiServices::class.java)
    }
}