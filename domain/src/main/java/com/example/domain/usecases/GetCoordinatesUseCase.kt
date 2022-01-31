package com.example.domain.usecases

import com.example.domain.model.geo.Coordinates
import com.example.domain.repository.ForecastWeatherRepository

class GetCoordinatesUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(locationName: String, limit: Int): List<Coordinates> {
        return repository.getCoordinatesByLocationName(locationName, limit)
    }
}