package ua.rikutou.studio.data.datasource.actor

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.ActorEntity

interface ActorDataSource {
    suspend fun getAll(studioId: Long): Flow<List<Actor>>
    suspend fun createUpdate(actorEntity: ActorEntity)
    suspend fun getById(actorId: Long): Flow<Actor>
    suspend fun delete(actorId: Long)
    suspend fun load(studioId: Long, search: String?)
}