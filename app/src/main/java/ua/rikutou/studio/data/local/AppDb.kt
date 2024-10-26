package ua.rikutou.studio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.rikutou.studio.data.local.dao.LocationDao
import ua.rikutou.studio.data.local.dao.StudioDao
import ua.rikutou.studio.data.local.dao.UserDao
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        StudioEntity::class,
        LocationEntity::class,
    ],
    version = 2,
)
abstract class AppDb : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val studioDao: StudioDao
    abstract val locationDao: LocationDao
}