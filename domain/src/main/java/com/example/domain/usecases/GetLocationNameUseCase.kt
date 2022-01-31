package com.example.domain.usecases

import com.example.domain.model.geo.LocationName
import com.example.domain.repository.ForecastWeatherRepository

class GetLocationNameUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(lat: Double, lon: Double, limit: Int): List<LocationName> {
        return repository.getLocationNameByCoordinates(lat, lon, limit)
    }
}