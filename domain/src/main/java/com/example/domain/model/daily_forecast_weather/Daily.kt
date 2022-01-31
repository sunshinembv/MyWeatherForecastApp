package com.example.domain.model.daily_forecast_weather

import com.example.domain.model.Weather

interface Daily {
    val date: Long
    val temp: Temperature
    val weather: List<Weather>
}