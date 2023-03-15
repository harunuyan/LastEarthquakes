package com.volie.lastearthquakes.repo

import com.volie.lastearthquakes.api.EarthquakeService
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: EarthquakeService
)