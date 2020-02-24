/**
Coder : AZhar
 **/


package com.example.weatherapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.Database.Dao.UserDao
import com.example.weatherapp.Database.Entity.CityNameHolder
import com.example.weatherapp.Database.Entity.FlagHandler

@Database(entities = [CityNameHolder::class, FlagHandler::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao

    companion object{
        @Volatile
        var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context):AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,AppDatabase::class.java,"AppDB"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}