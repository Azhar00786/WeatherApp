package com.example.weatherapp.database.database_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FlagHandler")
data class FlagHandler(@PrimaryKey var flag: Int)