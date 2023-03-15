package com.volie.lastearthquakes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.volie.lastearthquakes.model.Earthquake

@Database(entities = [Earthquake::class], version = 1)
@TypeConverters(Converter::class)
abstract class EarthquakeDatabase : RoomDatabase() {
    abstract fun getEarthquakeDao(): EarthquakeDao
}