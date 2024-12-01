package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.data.remote.location.dto.LocationRequest

@Entity
data class LocationEntity(
    @PrimaryKey val locationId: Long,
    val name: String,
    val address: String,
    val width: Float,
    val length: Float,
    val height: Float,
    val type: LocationType,
    val studioId: Long,
    val rentPrice: Float,
)

data class Location(
    @Embedded val location: LocationEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "galleryId",
        associateBy = Junction(LocationToGalleryEntity::class)
    ) val images: List<GalleryEntity>
)

fun LocationEntity.toDto() =
    LocationRequest(
        locationId = if(locationId < 0) null else locationId,
        name = name,
        address = address,
        width = width,
        height = height,
        length = length,
        type = type.name.lowercase(),
        studioId = studioId,
        rentPrice = rentPrice,
    )