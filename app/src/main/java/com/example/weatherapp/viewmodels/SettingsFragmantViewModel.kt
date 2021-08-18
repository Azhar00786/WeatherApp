/**
Coder : AZhar
 **/


package com.example.weatherapp.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.database.dao.UserDao
import com.example.weatherapp.repository.Repository

class SettingsFragmantViewModel(private val daoConnector: UserDao) : ViewModel() {
    private var repo: Repository = Repository(daoConnector)

    var flagT = MutableLiveData<Int>()

    suspend fun setDataInDb(location: String) {
        repo.insertCityNameInDb(location)
        flagT.value = 0
    }
}

class SettingsFragmantViewModelFactory(private val daoConnector: UserDao) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsFragmantViewModel(daoConnector) as T
    }
}