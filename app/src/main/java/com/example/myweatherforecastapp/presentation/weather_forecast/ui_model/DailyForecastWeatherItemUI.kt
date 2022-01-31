package com.example.myweatherforecastapp.presentation.weather_forecast.ui_model

data class DailyForecastWeatherItemUI(
    val id: Int,
    val date: String,
    val dayOfWeek: String,
    val icon: String,
    val dayDegrees: String,
    val nightDegrees: String,
)
