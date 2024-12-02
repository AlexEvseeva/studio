package ua.rikutou.studio.data.local

import androidx.room.TypeConverter
import ua.rikutou.studio.data.remote.location.LocationType
import java.util.Date

class DbConverters {
    @TypeConverter
    fun locationTypeToDb(type: LocationType): String = type.name.lowercase()

    @TypeConverter
    fun fromDbToLocationType(name: String): LocationType = LocationType.valueOf(name)

    @TypeConverter
    fun dataToDb(date: Date): Long = date.time

    @TypeConverter
    fun dateFromDb(time: Long): Date = Date(time)
}