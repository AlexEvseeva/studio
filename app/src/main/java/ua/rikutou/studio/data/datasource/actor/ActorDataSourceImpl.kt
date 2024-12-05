@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.rikutou.studio.data.datasource.actor

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
import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.ActorEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.actor.ActorApi
import ua.rikutou.studio.data.remote.actor.dto.toEntity
import ua.rikutou.studio.data.remote.phone.dto.toEntity
import ua.rikutou.studio.data.remote.phone.dto.toActorRefEntity
import ua.rikutou.studio.data.remote.film.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class ActorDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val actorApi: ActorApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
): ActorDataSource {
    private val TAG by lazy { ActorDataSourceImpl::class.simpleName }

    override suspend fun getAll(studioId: Long): Flow<List<Actor>> =
    dbDataSource.dbFlow
        .flatMapLatest { db -> db.actorDao.getAll(studioId = studioId) }
        .flowOn(dbDeliveryDispatcher)
        .catch { it.printStackTrace() }

    override suspend fun createUpdate(actorEntity: ActorEntity): Unit = withContext(Dispatchers.IO) {
        actorApi.createUpdate(
            body = actorEntity.toDto()
        ).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.actorDao.insert(
                        body()?.toEntity()?.let {
                            listOf(it)
                        } ?: emptyList()
                    )
                }
                else -> {
                    Log.e(TAG, "createUpdate: $actorEntity")
                }
            }
        }
    }

    override suspend fun getById(actorId: Long): Flow<Actor> =
        dbDataSource.dbFlow
            .flatMapLatest { db-> db.actorDao.getById(actorId = actorId) }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun delete(actorId: Long): Unit = withContext(Dispatchers.IO) {
        actorApi.delete(actorId = actorId).run {
            dbDataSource.db.actorDao.delete(actorId = actorId)
            when {
                isSuccessful -> {}
                else -> Log.e(TAG, "delete: $actorId")
            }
        }
    }

    override suspend fun load(studioId: Long, search: String?): Unit = withContext(Dispatchers.IO) {
        actorApi.getActors(studioId = studioId, search = if(search?.isEmpty() == true) null else search).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.actorDao.syncInsert(
                        body()?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.filmDao.insert(
                        body()
                            ?.map {
                                it.films
                            }
                            ?.filterNotNull()
                            ?.flatten()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.actorToFilm.insert(
                        body()
                            ?.map {
                                it.actorFilms
                            }
                            ?.filterNotNull()
                            ?.flatten()
                            ?.toSet()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.phoneDao.insert(
                        body()
                            ?.map { it.phones }
                            ?.filterNotNull()
                            ?.flatten()
                            ?.toSet()
                            ?.map { it.toEntity() } ?: emptyList()
                    )
                    dbDataSource.db.actorToPhoneDao.insert(
                        body()?.map { actor ->
                            actor.phones?.map {
                                it.toActorRefEntity(actorId = actor.actorId)
                            }
                        }?.filterNotNull()?.flatten() ?: emptyList()
                    )

                }
            }
        }
    }
}