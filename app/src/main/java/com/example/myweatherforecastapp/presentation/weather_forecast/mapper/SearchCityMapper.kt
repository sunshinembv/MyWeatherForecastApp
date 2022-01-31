package com.example.myweatherforecastapp.presentation.weather_forecast.mapper

import com.example.domain.model.geo.Coordinates
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.SearchCityItemUI
import javax.inject.Inject

class SearchCityMapper @Inject constructor() {

    fun fromCoordinates(coordinates: List<Coordinates>): List<SearchCityItemUI> {
        return coordinates.map { SearchCityItemUI(it.lat, it.lon, it.name, it.country, it.state) }
    }
}