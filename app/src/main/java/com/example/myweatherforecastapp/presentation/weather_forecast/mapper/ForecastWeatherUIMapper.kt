package com.example.myweatherforecastapp.presentation.weather_forecast.mapper

import com.example.domain.model.daily_forecast_weather.Daily
import com.example.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.DailyForecastWeatherItemUI
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.ForecastWeatherUI
import com.example.myweatherforecastapp.utils.pressureMbToMMhg
import com.example.myweatherforecastapp.utils.toDate
import java.util.*
import javax.inject.Inject

class ForecastWeatherUIMapper @Inject constructor() {

    fun fromDailyForecastWeather(
        dailyForecastWeather: DailyForecastWeather,
        locationName: String,
        datePattern: String = "dd MMMM",
        dayOfWeekPattern: String = "EEEE"
    ): ForecastWeatherUI {
        return ForecastWeatherUI(
            locationName,
            dailyForecastWeather.current.temp.toInt().toString(),
            dailyForecastWeather.current.weather.first().description.capitalize(Locale.ROOT),
            dailyForecastWeather.current.feelsLike.toInt().toString(),
            dailyForecastWeather.current.windSpeed.toString(),
            dailyForecastWeather.current.pressure.pressureMbToMMhg().toString(),
            dailyForecastWeather.current.humidity.toString(),
            dailyForecastWeather.current.weather.first().icon,
            dailyForecastWeather.daily.map { daily ->
                fromDaily(
                    daily,
                    datePattern,
                    dayOfWeekPattern
                )
            },
        )
    }

    private fun fromDaily(
        daily: Daily,
        datePattern: String,
        dayOfWeekPattern: String
    ): DailyForecastWeatherItemUI {
        return DailyForecastWeatherItemUI(
            0,
            daily.date.toDate(datePattern),
            daily.date.toDate(dayOfWeekPattern).capitalize(Locale.ROOT),
            daily.weather.first().icon,
            daily.temp.day.toInt().toString(),
            daily.temp.night.toInt().toString(),
        )
    }
}