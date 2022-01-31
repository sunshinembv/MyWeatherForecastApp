package com.example.data.model.geo

import com.example.domain.model.geo.Coordinates
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinatesData(
    override val lat: Double,
    override val lon: Double,
    override val name: String,
    override val country: String,
    override val state: String?
) : Coordinates
