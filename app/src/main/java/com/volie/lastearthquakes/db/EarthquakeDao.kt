package com.volie.lastearthquakes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.volie.lastearthquakes.model.Earthquake

@Dao
interface EarthquakeDao {

    @Query("SELECT * FROM earthquakes")
    fun getEarthquakes(): LiveData<List<Earthquake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(earthquake: Earthquake)

    @Delete
    suspend fun deleteEarthquake(earthquake: Earthquake)
}