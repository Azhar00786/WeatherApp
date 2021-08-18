package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.database.dao.UserDao
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.*

class MainFragmantViewModel(private val databaseConnect: UserDao) : ViewModel() {
    private var repo: Repository = Repository(databaseConnect)

    var locationQuery = MutableLiveData<String>()
    var weatherDescription = MutableLiveData<String>()
    var temperature = MutableLiveData<String>()
    var windSpeed = MutableLiveData<String>()
    var windDirection = MutableLiveData<String>()
    var humidity = MutableLiveData<String>()
    var cloudCover = MutableLiveData<String>()
    var uvIndex = MutableLiveData<String>()
    var visibility = MutableLiveData<String>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var localTime = MutableLiveData<String>()
    var imageUrl = MutableLiveData<String>()


    init {
        Log.d("MainFragmantViewModel", "viewmodel created")
    }

    suspend fun accessDatabase() = withContext(Dispatchers.Default) {
        repo.storeDataInDatabase()
    }

    suspend fun serverDataGetter() = withContext(Dispatchers.Main) {
        repo.accessServerData(
            locationQuery,
            weatherDescription,
            temperature,
            windSpeed,
            windDirection,
            humidity,
            cloudCover,
            uvIndex,
            visibility,
            latitude,
            longitude,
            localTime,
            imageUrl
        )
    }

    private suspend fun getFlagDbValue(): Int = withContext(Dispatchers.Default) {
        return@withContext databaseConnect.getAllFlagHandler()
    }
}

class MainFragmantViewModelFactory(private val databaseConnect: UserDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainFragmantViewModel(databaseConnect) as T
    }
}