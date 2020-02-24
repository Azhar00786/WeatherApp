/**
Coder : AZhar
 **/


package com.example.weatherapp.Entity

import com.squareup.moshi.Json

data class Current(@Json(name = "weather_descriptions")
                   val weatherDescriptions: List<String>?,
                   @Json(name = "observation_time")
                   val observationTime: String = "",
                   @Json(name = "wind_degree")
                   val windDegree: Int = 0,
                   @Json(name = "visibility")
                   val visibility: Int = 0,
                   @Json(name = "weather_icons")
                   val weatherIcons: List<String>?,
                   @Json(name = "feelslike")
                   val feelslike: Int = 0,
                   @Json(name = "is_day")
                   val isDay: String = "",
                   @Json(name = "wind_dir")
                   val windDir: String = "",
                   @Json(name = "pressure")
                   val pressure: Int = 0,
                   @Json(name = "cloudcover")
                   val cloudcover: Int = 0,
                   @Json(name = "precip")
                   val precip: Double = 0.0,
                   @Json(name = "uv_index")
                   val uvIndex: Int = 0,
                   @Json(name = "temperature")
                   val temperature: Int = 0,
                   @Json(name = "humidity")
                   val humidity: Int = 0,
                   @Json(name = "wind_speed")
                   val windSpeed: Int = 0,
                   @Json(name = "weather_code")
                   val weatherCode: Int = 0)