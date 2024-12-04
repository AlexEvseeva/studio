package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.rikutou.studio.data.local.entity.ActorToFilmEntity

@Dao
interface ActorToFilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ActorToFilmEntity>): List<Long>

    @Query("DELETE FROM actortofilmentity WHERE actorId=:actorId")
    suspend fun delete(actorId: Long)
}