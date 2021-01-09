/**
Coder : AZhar
 **/


package com.example.weatherapp.entity

import com.squareup.moshi.Json

data class WeatherRoot(
    @Json(name = "request")
    val request: Request,
    @Json(name = "current")
    val current: Current,
    @Json(name = "location")
    val location: Location
)