package ua.rikutou.studio.data.local

import androidx.room.TypeConverter
import ua.rikutou.studio.data.remote.location.LocationType

class DbConverters {
    @TypeConverter
    fun locationTypeToDb(type: LocationType): String = type.name.lowercase()

    @TypeConverter
    fun fromDbToLocationType(name: String): LocationType = LocationType.valueOf(name)
}