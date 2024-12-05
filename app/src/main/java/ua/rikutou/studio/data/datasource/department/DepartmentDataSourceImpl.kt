package ua.rikutou.studio.data.datasource.department

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.phone.dto.toEntity
import ua.rikutou.studio.data.remote.department.DepartmentApi
import ua.rikutou.studio.data.remote.department.dto.toEntity
import ua.rikutou.studio.data.remote.email.dto.toActorRerEntity
import ua.rikutou.studio.data.remote.email.dto.toDepartmentRefEntity
import ua.rikutou.studio.data.remote.email.dto.toEntity
import ua.rikutou.studio.data.remote.phone.dto.toDepartmentRefEntity
import ua.rikutou.studio.data.remote.section.dto.toEntity
import ua.rikutou.studio.data.remote.transport.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class DepartmentDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val departmentApi: DepartmentApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : DepartmentDataSource {
    private val TAG by lazy { DepartmentDataSourceImpl::class.simpleName }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllDepartments(studioId: Long): Flow<List<Department>> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.departmentDao.getAll(studioId = studioId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun loadDepartments(studioId: Long, search: String): Unit = withContext(Dispatchers.IO) {
        departmentApi.getDepartments(
            studioId = studioId
        ).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.departmentDao.insert(
                        body()?.map {
                            it.toEntity()
                        } ?: emptyList()
                    )
                    dbDataSource.db.sectionDao.syncInsert(
                        body()
                            ?.mapNotNull { it.sections }
                            ?.flatten()?.map { it.toEntity() } ?: emptyList<SectionEntity>()
                    )
                    dbDataSource.db.transportDao.syncInsert(
                        body()
                            ?.mapNotNull { it.transport }
                            ?.flatten()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.phoneDao.insert(
                        body()
                            ?.map {
                                it.phones
                            }
                            ?.filterNotNull()?.flatten()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.departToPhoneDao.insert(
                        body()
                            ?.map { dep ->
                                dep.phones?.map { it.toDepartmentRefEntity(departmentId = dep.departmentId) }
                            }
                            ?.filterNotNull()
                            ?.flatten() ?: emptyList()
                    )
                    dbDataSource.db.emailDao.insert(
                        body()
                            ?.map {
                                it.emails
                            }
                            ?.filterNotNull()
                            ?.flatten()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.departmentToEmailDao.insert(
                        body()
                            ?.map { dept ->
                                dept.emails?.map { it.toDepartmentRefEntity(departmentId = dept.departmentId) }
                            }
                            ?.filterNotNull()
                            ?.flatten() ?: emptyList()
                    )
                }
                else -> {
                    Log.e(TAG, "loadDepartments: studioId: $studioId, search: $search")
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getDepartmentById(departmentId: Long): Flow<Department> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.departmentDao.getDepartmentById(departmentId = departmentId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun save(department: DepartmentEntity): Unit = withContext(Dispatchers.IO) {
        departmentApi.saveUpdateDepartment(
            department.toDto().copy(
                departmentId = if(department.departmentId > 0) department.departmentId else null
            )
        ).run {
            when {
                isSuccessful -> {
                    body()?.let {
                        dbDataSource.db.departmentDao.insert(
                            listOf(it.toEntity())
                        )
                    }
                }
                else -> {
                    Log.e(TAG, "save: Error: deparmentId: ${department.departmentId}, type: ${department.type}")
                }
            }
        }
    }

    override suspend fun delete(departmentId: Long): Unit = withContext(Dispatchers.IO) {
        departmentApi.deleteDepartment(departmentId = departmentId).run {
            when {
                isSuccessful ->  {
                    dbDataSource.db.departmentDao.deleteById(departmentId = departmentId)
                }
            }
        }
    }
}