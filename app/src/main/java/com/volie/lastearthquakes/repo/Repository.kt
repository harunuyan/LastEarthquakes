package com.volie.lastearthquakes.repo

import com.volie.lastearthquakes.api.EarthquakeService
import com.volie.lastearthquakes.db.EarthquakeDao
import javax.inject.Inject

class Repository
@Inject constructor(
    private val service: EarthquakeService,
    private val dao: EarthquakeDao
)