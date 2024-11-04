package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class LocationEntity(
    @PrimaryKey val locationId: Long,
    val name: String,
    val address: String,
    val width: Float,
    val length: Float,
    val height: Float,
    val type: String,
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