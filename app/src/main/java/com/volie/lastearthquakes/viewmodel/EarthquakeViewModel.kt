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

    private val _news = MutableLiveData<Resource<EarthquakeWrapper>>()
    val news: LiveData<Resource<EarthquakeWrapper>> = _news

    private fun getEarthquakeFromDb(): LiveData<List<Earthquake>> {
        return repository.getEarthquakesFromDb()
    }

    private suspend fun getEarthquakeFromRemote(): Resource<EarthquakeWrapper> {
        return repository.getEarthquakesFromApi()
    }


    fun getEarthquakes() {
        _news.postValue(Resource.loading(null))
        viewModelScope.launch {
            val db = getEarthquakeFromDb()
            if (db.value.isNullOrEmpty()) {
                val remoteData = getEarthquakeFromRemote()
                _news.postValue(remoteData)
                return@launch
            }
        }
    }

    fun refreshEarthquakes() {
        _news.postValue(Resource.loading(null))
        viewModelScope.launch {
            val refreshData = repository.getEarthquakesFromApi()
            _news.postValue(refreshData)
        }
    }

    fun searchDatabase(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val search = repository.searchDatabase(searchQuery)
            _news.postValue(Resource.success(EarthquakeWrapper(search)))
        }
    }

    fun sortLowMag() {
        viewModelScope.launch(Dispatchers.IO) {
            val sortLow = repository.sortLowMag()
            _news.postValue(Resource.success(EarthquakeWrapper(sortLow)))
        }
    }

    fun sortHighMag() {
        viewModelScope.launch(Dispatchers.IO) {
            val sortHigh = repository.sortHighMag()
            _news.postValue(Resource.success(EarthquakeWrapper(sortHigh)))
        }
    }
}