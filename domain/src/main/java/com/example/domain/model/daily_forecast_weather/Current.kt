package com.example.domain.model.daily_forecast_weather

import com.example.domain.model.Weather

interface Current {
    val date: Long
    val temp: Double
    val feelsLike: Double
    val pressure: Int
    val humidity: Int
    val windSpeed: Double
    val weather: List<Weather>
}