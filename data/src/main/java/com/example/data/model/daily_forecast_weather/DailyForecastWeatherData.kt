package com.example.data.model.daily_forecast_weather

import com.example.domain.model.daily_forecast_weather.DailyForecastWeather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyForecastWeatherData(
    override val current: CurrentData,
    override val daily: List<DailyData>
) : DailyForecastWeather
