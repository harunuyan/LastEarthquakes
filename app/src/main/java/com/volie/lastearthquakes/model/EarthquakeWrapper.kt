package com.volie.lastearthquakes.model

import com.google.gson.annotations.SerializedName

data class EarthquakeWrapper(
    @SerializedName("result")
    val data: List<Earthquake>
)