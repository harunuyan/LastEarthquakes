package com.volie.lastearthquakes.repo

import com.volie.lastearthquakes.api.EarthquakeService
import com.volie.lastearthquakes.db.EarthquakeDao
import com.volie.lastearthquakes.model.Earthquake
import com.volie.lastearthquakes.model.EarthquakeWrapper
import com.volie.lastearthquakes.util.Resource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: EarthquakeService,
    private val dao: EarthquakeDao
) {
    suspend fun insertEarthquake(earthquakeList: List<Earthquake>) {
        dao.insert(earthquakeList)
    }

    suspend fun deleteEarthquake() {
        dao.deleteEarthquake()
    }

    suspend fun getEarthquakesFromDb() = dao.getEarthquakes()

    suspend fun getEarthquakesFromApi(): Resource<EarthquakeWrapper> {
        return try {
            val response = service.getEarthquakes()
            if (response.isSuccessful) {
                response.body()?.let {
                    insertEarthquake(it.data)
                    return@let Resource.success(it)
                } ?: Resource.error("Error!*1", null)
            } else {
                Resource.error("Error!*2", null)
            }
        } catch (e: Exception) {
            Resource.error("Error!*3${e}", null)
        }
    }

    fun searchDatabase(searchQuery: String): List<Earthquake> {
        return dao.searchDatabase(searchQuery)
    }

    suspend fun sortLowMag(): List<Earthquake> {
        return dao.sortLowMag()
    }

    suspend fun sortHighMag(): List<Earthquake> {
        return dao.sortHighMag()
    }
}