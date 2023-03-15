package com.volie.lastearthquakes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.lastearthquakes.model.Earthquake
import com.volie.lastearthquakes.model.EarthquakeWrapper
import com.volie.lastearthquakes.repo.Repository
import com.volie.lastearthquakes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarthquakeViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _earthquakes = MutableLiveData<Resource<EarthquakeWrapper>>()
    val earthquakes: LiveData<Resource<EarthquakeWrapper>> = _earthquakes

    private suspend fun getEarthquakeFromDb(): List<Earthquake> {
        return repository.getEarthquakesFromDb()
    }

    private suspend fun getEarthquakeFromRemote(): Resource<EarthquakeWrapper> {
        return repository.getEarthquakesFromApi()
    }


    fun getEarthquakes() {
        _earthquakes.postValue(Resource.loading(null))
        viewModelScope.launch {
            val db = getEarthquakeFromDb()
            if (db.isNullOrEmpty()) {
                val remoteData = getEarthquakeFromRemote()
                _earthquakes.postValue(remoteData)
                return@launch
            }
            _earthquakes.postValue(Resource.success(EarthquakeWrapper(db)))
        }
    }

    fun refreshEarthquakes() {
        _earthquakes.postValue(Resource.loading(null))
        viewModelScope.launch {
            repository.deleteEarthquake()
            val refreshData = getEarthquakeFromRemote()
            if (refreshData.data != null) {
                _earthquakes.postValue(refreshData)
                repository.insertEarthquake(refreshData.data.data)
            } else {
                val db = getEarthquakeFromDb()
                _earthquakes.postValue(Resource.success(EarthquakeWrapper(db)))
            }
        }
    }

    fun searchDatabase(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val search = repository.searchDatabase(searchQuery)
            _earthquakes.postValue(Resource.success(EarthquakeWrapper(search)))
        }
    }

    fun sortLowMag() {
        viewModelScope.launch(Dispatchers.IO) {
            val sortLow = repository.sortLowMag()
            _earthquakes.postValue(Resource.success(EarthquakeWrapper(sortLow)))
        }
    }

    fun sortHighMag() {
        viewModelScope.launch(Dispatchers.IO) {
            val sortHigh = repository.sortHighMag()
            _earthquakes.postValue(Resource.success(EarthquakeWrapper(sortHigh)))
        }
    }
}