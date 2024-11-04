package ua.rikutou.studio.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["locationId", "galleryId"])
data class LocationToGalleryEntity(
    val locationId: Long,
    val galleryId: Long,
)
