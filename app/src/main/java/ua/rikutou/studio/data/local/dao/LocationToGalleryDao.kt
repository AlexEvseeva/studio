package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ua.rikutou.studio.data.local.entity.LocationToGalleryEntity

@Dao
interface LocationToGalleryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<LocationToGalleryEntity>)
}