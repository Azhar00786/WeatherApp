/**
Coder : AZhar
 **/


package com.example.weatherapp.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.database.dao.UserDao
import com.example.weatherapp.database.database_entity.CityNameHolder
import kotlinx.coroutines.runBlocking

class SettingsFragmantViewModel(val daoConnector: UserDao) : ViewModel() {
    private val cityNo = 1
    var flagT = MutableLiveData<Int>()

    fun setDataInDb(location: String) = runBlocking {
        daoConnector.deleteCityName()
        daoConnector.insertCityName(CityNameHolder(cityNo, location))
        flagT.value = 0
    }
}

class SettingsFragmantViewModelFactory(private val daoConnector: UserDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsFragmantViewModel(daoConnector) as T
    }
}