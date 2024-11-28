package ua.rikutou.studio.data.datasource.section

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
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.section.SectionApi
import ua.rikutou.studio.data.remote.section.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class SectionDataSourceImpl(
    private val sectionApi: SectionApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : SectionDataSource{
    private val TAG by lazy { SectionDataSourceImpl::class.simpleName }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getSectionById(sectionId: Long): Flow<SectionEntity> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.sectionDao.getSectionById(sectionId = sectionId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun deleteSectionById(sectionId: Long): Unit = withContext(Dispatchers.IO) {
        try {
            sectionApi.deleteSectionById(sectionId = sectionId).run {
                dbDataSource.db.sectionDao.deleteById(sectionId)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override suspend fun createUpdateSection(sectionEntity: SectionEntity): Unit = withContext(Dispatchers.IO) {
        sectionApi.insertUpdateSection(
            body = sectionEntity.toDto()
        ).run {
            when {
                isSuccessful -> {
                    body()?.toEntity()?.let {
                        dbDataSource.db.sectionDao.insert(listOf(it))
                    }
                }
                else -> {
                    Log.e(TAG, "createUpdateSection: Error: id: ${sectionEntity.sectionId}, title: ${sectionEntity.title}", )
                }
            }
        }
    }
}