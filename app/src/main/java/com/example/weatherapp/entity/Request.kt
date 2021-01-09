/**
Coder : AZhar
 **/


package com.example.weatherapp.entity

import com.squareup.moshi.Json

data class Request(
    @Json(name = "unit")
    val unit: String = "",
    @Json(name = "query")
    val query: String = "",
    @Json(name = "language")
    val language: String = "",
    @Json(name = "type")
    val type: String = ""
)