package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.ActorEntity

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ActorEntity>): List<Long>

    suspend fun syncInsert(list:List<ActorEntity>) {
        deleteExcluded(
            insert(list)
        )
    }

    @Query("DELETE FROM actorentity WHERE actorId=:actorId")
    suspend fun delete(actorId: Long)

    @Query("DELETE FROM actorentity WHERE actorId NOT IN (:list)")
    suspend fun deleteExcluded(list: List<Long>)

    @Query("SELECT * FROM actorentity WHERE studioId=:studioId")
    fun getAll(studioId: Long): Flow<List<Actor>>

    @Query("SELECT * FROM actorentity where actorId=:actorId")
    fun getById(actorId: Long): Flow<Actor>
}