package ua.rikutou.studio.data.datasource.film

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Film

interface FilmDataSource {
    suspend fun getById(filmId: Long): Flow<Film>
}