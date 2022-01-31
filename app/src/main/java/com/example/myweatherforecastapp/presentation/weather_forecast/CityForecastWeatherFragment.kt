package com.example.myweatherforecastapp.presentation.weather_forecast

import android.os.Bundle
import android.view.View
import com.example.myweatherforecastapp.BaseFragment
import com.example.myweatherforecastapp.R

class CityForecastWeatherFragment : BaseFragment(R.layout.fragment_forecast_weather) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val latitude = requireArguments().getDouble(LOCATION_LATITUDE)
        val longitude = requireArguments().getDouble(LOCATION_LONGITUDE)
        forecastWeatherViewModel.getCurrentForecastWeather(latitude, longitude)
    }

    companion object {
        const val LOCATION_LATITUDE = "locationLatitude"
        const val LOCATION_LONGITUDE = "locationLongitude"
        fun args(latitude: Double, longitude: Double): Bundle {
            return Bundle().apply {
                putDouble(LOCATION_LATITUDE, latitude)
                putDouble(LOCATION_LONGITUDE, longitude)
            }
        }
    }
}