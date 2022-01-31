package com.example.data.repository

import com.example.data.NetworkHandleService
import com.example.data.network.WeatherApi
import com.example.domain.Result
import com.example.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.domain.model.geo.Coordinates
import com.example.domain.model.geo.LocationName
import com.example.domain.repository.ForecastWeatherRepository
import javax.inject.Inject

class ForecastWeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val networkHandleService: NetworkHandleService
) : ForecastWeatherRepository {

    override suspend fun getDailyForecastWeatherDataByCoordinates(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ): DailyForecastWeather {
        return when (val result = networkHandleService.apiCall {
            weatherApi.getDailyForecastWeatherByCoordinates(
                lat,
                lon,
                exclude,
                units
            )
        }) {
            is Result.Success -> result.data
            is Result.Error -> error(result.error)
            else -> throw IllegalStateException()
        }
    }

    override suspend fun getCoordinatesByLocationName(
        locationName: String,
        limit: Int
    ): List<Coordinates> {
        return when (val result = networkHandleService.apiCall {
            weatherApi.getCoordinatesByLocationName(
                locationName,
                limit
            )
        }) {
            is Result.Success -> result.data
            is Result.Error -> error(result.error)
            else -> throw IllegalStateException()
        }
    }

    override suspend fun getLocationNameByCoordinates(
        lat: Double,
        lon: Double,
        limit: Int
    ): List<LocationName> {
        return when (val result = networkHandleService.apiCall {
            weatherApi.getLocationNameByCoordinates(
                lat,
                lon,
                limit
            )
        }) {
            is Result.Success -> result.data
            is Result.Error -> error(result.error)
            else -> throw IllegalStateException()
        }
    }
}