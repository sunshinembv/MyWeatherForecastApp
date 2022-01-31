package com.example.domain.model.daily_forecast_weather

interface DailyForecastWeather {
    val current: Current
    val daily: List<Daily>
}