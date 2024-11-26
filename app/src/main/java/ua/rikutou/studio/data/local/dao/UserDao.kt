package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<UserEntity>)

    @Query("SELECT * FROM userentity WHERE userId=:userId LIMIT 1")
    fun getById(userId: Long): Flow<UserEntity>

    @Query("SELECT * FROM userentity")
    fun getAll(): Flow<List<UserEntity>>

}