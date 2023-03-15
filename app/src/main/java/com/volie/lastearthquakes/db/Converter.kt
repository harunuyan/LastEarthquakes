package com.volie.lastearthquakes.db

import androidx.room.TypeConverter
import com.volie.lastearthquakes.model.GeoJson

class Converter {
    @TypeConverter
    fun fromList(geoJson: GeoJson): String {
        return "${geoJson.coordinates[0]},${geoJson.coordinates[1]}"
    }

    @TypeConverter
    fun fromString(string: String): GeoJson {
        val double = string.split(",").map { it.toDouble() }
        return GeoJson(
            double
        )
    }
}