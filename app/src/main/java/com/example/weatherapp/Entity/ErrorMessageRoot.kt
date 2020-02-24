/**
Coder : AZhar
 **/


package com.example.weatherapp.Entity

import com.squareup.moshi.Json

data class ErrorMessageRoot(@Json(name = "success")
                            val success: Boolean = false,
                            @Json(name = "error")
                            val error: Error)