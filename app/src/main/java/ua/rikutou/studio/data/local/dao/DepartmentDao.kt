package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.DepartmentEntity

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DepartmentEntity>)

    @Query("SELECT * FROM departmententity WHERE studioId=:studioId")
    fun getAll(studioId: Long): Flow<List<DepartmentEntity>>
}