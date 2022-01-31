package com.example.domain.usecases

import com.example.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.domain.repository.ForecastWeatherRepository

class GetDailyForecastWeatherUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ): DailyForecastWeather {
        return repository.getDailyForecastWeatherDataByCoordinates(lat, lon, exclude, units)
    }
}