package com.volie.lastearthquakes.api

import com.volie.lastearthquakes.model.EarthquakeWrapper
import retrofit2.Response
import retrofit2.http.GET

interface EarthquakeService {

    @GET("/deprem/kandilli/live")
    suspend fun getEarthquakes(): Response<EarthquakeWrapper>
}