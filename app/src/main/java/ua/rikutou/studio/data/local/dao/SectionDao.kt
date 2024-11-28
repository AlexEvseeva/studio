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
    suspend fun syncInsert(list: List<SectionEntity>) {
        val inserted = insert(list)
        deleteExcludeByIds(inserted)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<SectionEntity>): List<Long>

    @Query("SELECT * FROM sectionentity WHERE sectionId=:sectionId")
    fun getSectionById(sectionId: Long): Flow<SectionEntity>

    @Query("DELETE FROM sectionentity WHERE sectionId=:sectionId")
    suspend fun deleteById(sectionId: Long)

    @Query("DELETE FROM sectionentity WHERE sectionId NOT IN (:list)")
    suspend fun deleteExcludeByIds(list: List<Long>)
}