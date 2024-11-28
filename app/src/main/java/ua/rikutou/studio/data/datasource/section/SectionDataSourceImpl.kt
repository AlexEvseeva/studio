package ua.rikutou.studio.data.datasource.section

import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.data.remote.section.SectionApi
import ua.rikutou.studio.di.DbDeliveryDispatcher

class SectionDataSourceImpl(
    private val sectionApi: SectionApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : SectionDataSource{

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getSectionById(sectionId: Long): Flow<SectionEntity> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.sectionDao.getSectionById(sectionId = sectionId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun deleteSectionById(sectionId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSection(sectionEntity: SectionEntity) {
        TODO("Not yet implemented")
    }
}