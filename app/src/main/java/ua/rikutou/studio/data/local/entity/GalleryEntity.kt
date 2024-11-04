package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GalleryEntity(
    @PrimaryKey val galleryId: Long,
    val url: String,
)
