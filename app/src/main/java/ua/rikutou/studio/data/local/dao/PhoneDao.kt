package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.rikutou.studio.data.local.entity.PhoneEntity

@Dao
interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<PhoneEntity>): List<Long>

    @Query("DELETE FROM phoneentity WHERE phoneId=:phoneId")
    suspend fun delete(phoneId: Long)
}