/**
Coder : AZhar
 **/


package com.example.weatherapp.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CityNameHolder")
data class CityNameHolder(
    @PrimaryKey
    val cityNo:Int,val cityName : String){

}