package ua.rikutou.studio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.rikutou.studio.data.local.dao.ActorDao
import ua.rikutou.studio.data.local.dao.DepartmentDao
import ua.rikutou.studio.data.local.dao.EquipmentDao
import ua.rikutou.studio.data.local.dao.GalleryDao
import ua.rikutou.studio.data.local.dao.LocationDao
import ua.rikutou.studio.data.local.dao.LocationToGalleryDao
import ua.rikutou.studio.data.local.dao.SectionDao
import ua.rikutou.studio.data.local.dao.StudioDao
import ua.rikutou.studio.data.local.dao.TransportDao
import ua.rikutou.studio.data.local.dao.UserDao
import ua.rikutou.studio.data.local.entity.ActorEntity
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.GalleryEntity
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.LocationToGalleryEntity
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        StudioEntity::class,
        LocationEntity::class,
        GalleryEntity::class,
        LocationToGalleryEntity::class,
        DepartmentEntity::class,
        SectionEntity::class,
        EquipmentEntity::class,
        TransportEntity::class,
        ActorEntity::class
    ],
    version = 12,
)
@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val studioDao: StudioDao
    abstract val locationDao: LocationDao
    abstract val galleryDao: GalleryDao
    abstract val locationToGalleryDao: LocationToGalleryDao
    abstract val departmentDao: DepartmentDao
    abstract val sectionDao: SectionDao
    abstract val equipmentDao: EquipmentDao
    abstract val transportDao: TransportDao
    abstract val actorDao: ActorDao
}