package com.example.data.model

import com.example.domain.model.Weather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherData(
    override val id: Int,
    override val main: String,
    override val description: String,
    override val icon: String
) : Weather
