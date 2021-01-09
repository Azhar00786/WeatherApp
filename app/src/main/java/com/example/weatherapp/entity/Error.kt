/**
Coder : AZhar
 **/


package com.example.weatherapp.entity

import com.squareup.moshi.Json

data class Error(
    @Json(name = "code")
    val code: Int = 0,
    @Json(name = "type")
    val type: String = "",
    @Json(name = "info")
    val info: String = ""
)