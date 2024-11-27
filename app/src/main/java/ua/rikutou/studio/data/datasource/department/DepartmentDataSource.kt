package ua.rikutou.studio.data.datasource.department

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.DepartmentEntity

interface DepartmentDataSource {
    suspend fun getAllDepartments(studioId: Long): Flow<List<DepartmentEntity>>
    suspend fun loadDepartments(studioId: Long, search: String)
    suspend fun getDepartmentById(departmentId: Long): Flow<DepartmentEntity>
    suspend fun save(department: DepartmentEntity)
    suspend fun delete(departmentId: Long)
}