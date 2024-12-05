package ua.rikutou.studio.data.datasource.film

import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.Film
import ua.rikutou.studio.di.DbDeliveryDispatcher

class FilmDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
): FilmDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getById(filmId: Long): Flow<Film> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.filmDao.getById(filmId = filmId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }
}