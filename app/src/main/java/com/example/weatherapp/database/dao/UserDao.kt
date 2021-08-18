package com.example.weatherapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.database.database_entity.CityNameHolder
import com.example.weatherapp.database.database_entity.FlagHandler

@Dao
interface UserDao {

    //Queries for CityNameHolderEntity

    @Insert
    suspend fun insertCityName(cityName: CityNameHolder)

    @Query("select cityName from CityNameHolder where cityNo = 1")
    suspend fun getCityName(): String

    @Query("delete from CityNameHolder")
    suspend fun deleteCityName()

    @Query("select distinct cityNo from CityNameHolder where cityNo = 1")
    suspend fun getCityNumberCount(): Int

    //Queries for FlagHandler

    @Insert
    suspend fun insertFlagValue(flagNo: FlagHandler)

    @Query("delete from FlagHandler")
    suspend fun deleteFlagValue()

    @Query("select ALL flag from FlagHandler")
    suspend fun getAllFlagHandler(): Int

    @Query("select distinct flag from FlagHandler where flag = 1")
    suspend fun selectFlagValue(): Int

    @Query("select distinct flag from FlagHandler where flag = 0")
    suspend fun selectFlagValueZero(): Int
}