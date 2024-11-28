package ua.rikutou.studio.data.datasource.department

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity

interface DepartmentDataSource {
    suspend fun getAllDepartments(studioId: Long): Flow<List<Department>>
    suspend fun loadDepartments(studioId: Long, search: String)
    suspend fun getDepartmentById(departmentId: Long): Flow<Department>
    suspend fun save(department: DepartmentEntity)
    suspend fun delete(departmentId: Long)
}