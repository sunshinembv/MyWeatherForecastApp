package com.example.domain.repository

import com.example.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.domain.model.geo.Coordinates
import com.example.domain.model.geo.LocationName

interface ForecastWeatherRepository {
    suspend fun getDailyForecastWeatherDataByCoordinates(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ): DailyForecastWeather

    suspend fun getCoordinatesByLocationName(locationName: String, limit: Int): List<Coordinates>

    suspend fun getLocationNameByCoordinates(
        lat: Double,
        lon: Double,
        limit: Int
    ): List<LocationName>
}