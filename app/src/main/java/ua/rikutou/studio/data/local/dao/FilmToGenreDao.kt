package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.rikutou.studio.data.local.entity.FilmToGenreEntity

@Dao
interface FilmToGenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<FilmToGenreEntity>)

    @Query("DELETE FROM FilmToGenreEntity WHERE filmId=:filmId")
    suspend fun delete(filmId: Long)
}