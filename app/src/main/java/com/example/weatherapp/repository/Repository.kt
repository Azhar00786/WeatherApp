package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.database.dao.UserDao
import com.example.weatherapp.database.database_entity.CityNameHolder
import com.example.weatherapp.database.database_entity.FlagHandler
import com.example.weatherapp.network.WeatherApi
import com.squareup.moshi.JsonDataException
import java.lang.Exception

class Repository(private val databaseConnector: UserDao) {
    private lateinit var cityName: String

    suspend fun storeDataInDatabase() {
        try {
            cityName = databaseConnector.getCityName()
            Log.d("cityName", cityName)
        } catch (e: Exception) {
            Log.e("ERROR", e.toString())
            databaseConnector.insertCityName(CityNameHolder(1, "Pune"))
            cityName = databaseConnector.getCityName()
            databaseConnector.insertFlagValue(FlagHandler(0))
        }
    }

    suspend fun accessServerData(
        locationQuery: MutableLiveData<String>,
        weatherDescription: MutableLiveData<String>,
        temperature: MutableLiveData<String>,
        windSpeed: MutableLiveData<String>,
        windDirection: MutableLiveData<String>,
        humidity: MutableLiveData<String>,
        cloudCover: MutableLiveData<String>,
        uvIndex: MutableLiveData<String>,
        visibility: MutableLiveData<String>,
        latitude: MutableLiveData<String>,
        longitude: MutableLiveData<String>,
        localTime: MutableLiveData<String>,
        imageUrl: MutableLiveData<String>
    ) {
        val apiService = WeatherApi.invoke()
        try {
            val response = apiService.getWeatherMapAsync(cityName).await()
            databaseConnector.deleteFlagValue()
            databaseConnector.insertFlagValue(FlagHandler(0))

            //for exactLocation
            locationQuery.value = response.request.query

            //for weatherDescription
            weatherDescription.value =
                response.current.weatherDescriptions.toString().replace("[", "")
                    .replace("]", "")

            //for temperature
            temperature.value = response.current.temperature.toString()

            //for WindSpeed
            windSpeed.value = response.current.windSpeed.toString()

            //for WindDirections
            windDirection.value = response.current.windDir.toString()

            //for Humidity
            humidity.value = response.current.humidity.toString()

            //for CloudCover
            cloudCover.value = response.current.cloudcover.toString()

            //for uvIndex
            uvIndex.value = response.current.uvIndex.toString()

            //for visibiliy
            visibility.value = response.current.visibility.toString()

            //for Latitude
            latitude.value = response.location.lat.toString()

            //for Longitude
            longitude.value = response.location.lon.toString()

            //for localTime
            localTime.value = response.location.localtime.toString()

            //for ImageURL
            imageUrl.value =
                response.current.weatherIcons.toString().replace("[", "").replace("]", "")

        } catch (e: JsonDataException) {
            Log.e("Error", e.toString())
            val response = apiService.getErrorMessageAsync(cityName).await()
            if (response.error.type == "request_failed") {
                databaseConnector.deleteFlagValue()
                databaseConnector.insertFlagValue(FlagHandler(1))
            }
            databaseConnector.deleteCityName()
        }
    }

    suspend fun insertCityNameInDb(location: String) {
        databaseConnector.deleteCityName()
        databaseConnector.insertCityName(CityNameHolder(1, location))
    }
}