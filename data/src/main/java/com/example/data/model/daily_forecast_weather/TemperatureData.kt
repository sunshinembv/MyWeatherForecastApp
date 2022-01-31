package com.example.data.model.daily_forecast_weather

import com.example.domain.model.daily_forecast_weather.Temperature
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TemperatureData(override val day: Double, override val night: Double) : Temperature