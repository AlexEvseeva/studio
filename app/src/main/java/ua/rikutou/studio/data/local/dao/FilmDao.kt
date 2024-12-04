package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.rikutou.studio.data.local.entity.FilmEntity

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<FilmEntity>): List<Long>

    @Query("DELETE FROM FilmEntity WHERE filmId=:filmId")
    suspend fun delete(filmId: Long)
}