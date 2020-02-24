/**
Coder : AZhar
 **/


package com.example.weatherapp.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FlagHandler")
data class FlagHandler(@PrimaryKey var flag : Int)