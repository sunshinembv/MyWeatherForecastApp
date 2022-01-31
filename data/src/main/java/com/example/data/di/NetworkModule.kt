package com.example.data.di

import com.example.data.network.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).addNetworkInterceptor { chain ->
                val modifiedRequest = chain.request().newBuilder()
                    .addHeader(HEADER_API_KEY, "").build()

                chain.proceed(modifiedRequest)
            }
            .build()

        val retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create()
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"
        private const val HEADER_API_KEY = "X-Api-Key"
    }
}