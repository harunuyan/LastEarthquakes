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

    fun getEarthquakes() {
        viewModelScope.launch {
            val db = getEarthquakeFromDb()
            if (db.value.isNullOrEmpty()) {
                val remoteData = repository.getEarthquakesFromApi()
                _news.postValue(remoteData)
                return@launch
            }
        }
    }
}