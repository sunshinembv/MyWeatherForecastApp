package com.example.myweatherforecastapp.presentation.weather_forecast.ui_model

data class ForecastWeatherUI(
    val locationName: String,
    val degrees: String,
    val description: String,
    val feelsLike: String,
    val wind: String,
    val pressure: String,
    val humidity: String,
    val icon: String,
    val dailyDailyForecast: List<DailyForecastWeatherItemUI>,
)
