package com.example.myweatherforecastapp.presentation.weather_forecast.ui_model

data class SearchCityItemUI(
    val lat: Double,
    val lon: Double,
    val name: String,
    val country: String,
    val state: String?
)