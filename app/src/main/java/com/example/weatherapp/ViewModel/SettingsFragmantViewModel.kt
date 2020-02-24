/**
Coder : AZhar
 **/


package com.example.weatherapp.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Database.Dao.UserDao
import com.example.weatherapp.Database.Entity.CityNameHolder
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

class SettingsFragmantViewModelFactory(private val daoConnector : UserDao) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsFragmantViewModel(daoConnector) as T
    }
}