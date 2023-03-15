package com.volie.lastearthquakes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volie.lastearthquakes.model.Earthquake

@Dao
interface EarthquakeDao {

    @Query("SELECT * FROM earthquakes")
    suspend fun getEarthquakes(): List<Earthquake>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Earthquake>)
}