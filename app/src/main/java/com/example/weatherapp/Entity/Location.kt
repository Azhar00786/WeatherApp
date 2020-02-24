/**
Coder : AZhar
 **/


package com.example.weatherapp.Entity

import com.squareup.moshi.Json

data class Location(@Json(name = "localtime")
                    val localtime: String = "",
                    @Json(name = "utc_offset")
                    val utcOffset: String = "",
                    @Json(name = "country")
                    val country: String = "",
                    @Json(name = "localtime_epoch")
                    val localtimeEpoch: Int = 0,
                    @Json(name = "name")
                    val name: String = "",
                    @Json(name = "timezone_id")
                    val timezoneId: String = "",
                    @Json(name = "lon")
                    val lon: String = "",
                    @Json(name = "region")
                    val region: String = "",
                    @Json(name = "lat")
                    val lat: String = "")