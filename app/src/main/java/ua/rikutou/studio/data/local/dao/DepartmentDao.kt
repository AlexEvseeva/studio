package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.navigation.Screen

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DepartmentEntity>)

    @Query("SELECT * FROM departmententity WHERE studioId=:studioId")
    fun getAll(studioId: Long): Flow<List<Department>>

    @Query("SELECT * FROM departmententity WHERE departmentId=:departmentId")
    fun getDepartmentById(departmentId: Long): Flow<Department>

    @Query("DELETE FROM departmententity WHERE departmentId=:departmentId")
    fun deleteById(departmentId: Long)
}