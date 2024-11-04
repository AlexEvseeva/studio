package ua.rikutou.studio.data.remote.gallery.dto

import ua.rikutou.studio.data.local.entity.GalleryEntity

data class GalleryResponse(
    val galleryId: Long,
    val url: String,
)

fun GalleryResponse.toEntity() =
    GalleryEntity(
        galleryId = galleryId,
        url = url,
    )