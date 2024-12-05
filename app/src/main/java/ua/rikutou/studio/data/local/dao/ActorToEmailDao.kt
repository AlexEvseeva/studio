package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ua.rikutou.studio.data.local.entity.ActorToEmailEntity

@Dao
interface ActorToEmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ActorToEmailEntity>): List<Long>
}