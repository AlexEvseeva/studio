package ua.rikutou.studio.data.local

import androidx.room.TypeConverter
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import ua.rikutou.studio.data.remote.equipment.toEquipmentType
import ua.rikutou.studio.data.remote.film.Genre
import ua.rikutou.studio.data.remote.film.toGenre
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.data.remote.transport.toTransportType
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

    @TypeConverter
    fun transportTypeToDb(type: TransportType): Int = type.fromTransportType()

    @TypeConverter
    fun transportTypeFromDb(num: Int): TransportType = num.toTransportType()

    @TypeConverter
    fun genreToDb(genre: Genre) = genre.fromGenre()

    @TypeConverter
    fun genreFromDb(num: Int) = num.toGenre()

    @TypeConverter
    fun equipmentToDb(type: EquipmentType): Int = type.toDb()

    @TypeConverter
    fun equipmentFromDb(num: Int) = num.toEquipmentType()
}