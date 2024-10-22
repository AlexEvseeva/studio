package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.StudioEntity

@Dao
interface StudioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(studioEntity: StudioEntity): Long

    @Query("SELECT * FROM studioentity WHERE studioId = :studioId LIMIT 1")
    fun getById(studioId: Long): Flow<StudioEntity?>
}