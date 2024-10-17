package ua.rikutou.studio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.rikutou.studio.data.local.dao.UserDao
import ua.rikutou.studio.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
               ],
    version = 1,
)
abstract class AppDb : RoomDatabase() {
    abstract val userDao: UserDao
}