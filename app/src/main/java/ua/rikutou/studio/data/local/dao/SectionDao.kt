package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.SectionEntity

@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<SectionEntity>)

    @Query("SELECT * FROM sectionentity WHERE sectionId=:sectionId")
    fun getSectionById(sectionId: Long): Flow<SectionEntity>

    @Query("DELETE FROM sectionentity WHERE sectionId=:sectionId")
    suspend fun deleteById(sectionId: Long)
}