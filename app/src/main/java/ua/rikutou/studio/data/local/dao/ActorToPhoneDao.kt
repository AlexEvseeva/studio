package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ua.rikutou.studio.data.local.entity.ActorToPhoneEntity

@Dao
interface ActorToPhoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ActorToPhoneEntity>): List<Long>
}