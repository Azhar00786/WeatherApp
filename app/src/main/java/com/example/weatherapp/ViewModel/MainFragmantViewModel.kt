/**
Coder : AZhar
 **/


package com.example.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.Database.Dao.UserDao
import com.example.weatherapp.Database.Entity.CityNameHolder
import com.example.weatherapp.Database.Entity.FlagHandler
import com.example.weatherapp.network.WeatherApi
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import java.lang.Exception

class MainFragmantViewModel(val databaseConnect: UserDao) :ViewModel(){

    var locationQuery = MutableLiveData<String>()
    var weatherDescription = MutableLiveData<String>()
    var temperature = MutableLiveData<String>()
    var windSpeed = MutableLiveData<String>()
    var windDirection = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var cloudCover =MutableLiveData<String>()
    var uvIndex = MutableLiveData<String>()
    var visibility = MutableLiveData<String>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var localTime = MutableLiveData<String>()
    var imageUrl = MutableLiveData<String>()
    //var error = MutableLiveData<Int>()

    lateinit var cityName : String

    val viewM = Job()
    val coroutineScope = CoroutineScope(viewM + Dispatchers.Main)

    init {
        Log.d("MainFragmantViewModel","viewmodel created")
    }

    fun handlerForDbData() = runBlocking{
        try {
            cityName = databaseConnect.getCityName()
            Log.d("cityName",cityName)
        }catch (e:Exception){
            Log.e("ERROR",e.toString())
            databaseConnect.insertCityName(CityNameHolder(1,"Pune"))
            cityName = databaseConnect.getCityName()
            databaseConnect.insertFlagValue(FlagHandler(0))
            //serverDataGetter()
        }
    }

    fun serverDataGetter(){
        coroutineScope.launch {
            val apiService = WeatherApi.invoke()
            try {
                val response = apiService.getWeatherMap(cityName).await()
                databaseConnect.deleteFlagValue()
                databaseConnect.insertFlagValue(FlagHandler(0))

                //for exactLocation
                locationQuery.value = response.request.query

                //for weatherDescription
                weatherDescription.value = response.current.weatherDescriptions.toString().replace("[","").replace("]","")

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
                imageUrl.value = response.current.weatherIcons.toString().replace("[","").replace("]","")

            }catch (e : JsonDataException){
                Log.e("Error###",e.toString())
                val response = apiService.getErrorMessage(cityName).await()
                if(response.error.type == "request_failed"){
                    databaseConnect.deleteFlagValue()
                    databaseConnect.insertFlagValue(FlagHandler(1))
                    //error.value = databaseConnect.selectFlagValue()
                }
                databaseConnect.deleteCityName()
            }
        }
    }

    fun getFlagDbValue():Int = runBlocking{
        return@runBlocking databaseConnect.getAllFlagHandler()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainFragmantViewModel","ViewModel is destroyed!")
    }
}

class MainFragmantViewModelFactory(val databaseConnect : UserDao) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainFragmantViewModel(databaseConnect) as T
    }
}