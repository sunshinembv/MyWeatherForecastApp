package com.example.data.model.daily_forecast_weather

import com.example.data.model.WeatherData
import com.example.domain.model.daily_forecast_weather.Daily
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyData(
    @Json(name = "dt")
    override val date: Long,
    override val temp: TemperatureData,
    override val weather: List<WeatherData>
) : Daily
