package ua.rikutou.studio.data.datasource.section

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.SectionEntity

interface SectionDataSource {
    suspend fun getSectionById(sectionId: Long): Flow<SectionEntity>
    suspend fun deleteSectionById(sectionId: Long)
    suspend fun updateSection(sectionEntity: SectionEntity)
}