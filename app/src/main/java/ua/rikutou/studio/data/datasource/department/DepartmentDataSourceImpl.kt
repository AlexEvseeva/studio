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
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.remote.department.DepartmentApi
import ua.rikutou.studio.data.remote.department.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class DepartmentDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val departmentApi: DepartmentApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : DepartmentDataSource {
    private val TAG by lazy { DepartmentDataSourceImpl::class.simpleName }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllDepartments(studioId: Long): Flow<List<DepartmentEntity>> =
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
                }
                else -> {
                    Log.e(TAG, "loadDepartments: studioId: $studioId, search: $search")
                }
            }
        }
    }
}