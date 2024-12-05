package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.LocationSelectionEntity


@Dao
interface LocationSelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LocationSelectionEntity)

    @Query("DELETE FROM locationselectionentity WHERE locationId=:locationId")
    suspend fun deleteById(locationId: Long)

    @Query("SELECT locationId FROM locationselectionentity")
    fun getSelected(): Flow<List<Long>>
}