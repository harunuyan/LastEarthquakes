package com.volie.lastearthquakes.db

import androidx.room.*
import com.volie.lastearthquakes.model.Earthquake

@Dao
interface EarthquakeDao {

    @Query("SELECT * FROM earthquakes")
    suspend fun getEarthquakes(): List<Earthquake>

    @Query("SELECT * FROM earthquakes WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchDatabase(searchQuery: String): List<Earthquake>

    @Query("SELECT * FROM earthquakes ORDER BY magnitude DESC")
    suspend fun sortHighMag(): List<Earthquake>

    @Query("SELECT * FROM earthquakes ORDER BY magnitude ASC")
    suspend fun sortLowMag(): List<Earthquake>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(earthquakeList: List<Earthquake>)

    @Delete
    suspend fun deleteEarthquake(earthquake: Earthquake)
}