package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<LocationEntity>)

    @Query("SELECT * FROM locationentity WHERE studioId = :studioId")
    fun getByStudioId(studioId: Long): Flow<List<Location>>

    @Query("SELECT * FROM locationentity WHERE locationId = :locationId LIMIT 1")
    fun getByLocationId(locationId: Long): Flow<Location>
}