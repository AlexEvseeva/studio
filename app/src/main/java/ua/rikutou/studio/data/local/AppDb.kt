package ua.rikutou.studio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.rikutou.studio.data.local.dao.ActorDao
import ua.rikutou.studio.data.local.dao.ActorToEmailDao
import ua.rikutou.studio.data.local.dao.ActorToFilmDao
import ua.rikutou.studio.data.local.dao.ActorToPhoneDao
import ua.rikutou.studio.data.local.dao.DepartmentDao
import ua.rikutou.studio.data.local.dao.DepartmentToEmailDao
import ua.rikutou.studio.data.local.dao.DepartmentToPhoneDao
import ua.rikutou.studio.data.local.dao.EmailDao
import ua.rikutou.studio.data.local.dao.EquipmentDao
import ua.rikutou.studio.data.local.dao.EquipmentSelectionDao
import ua.rikutou.studio.data.local.dao.FilmDao
import ua.rikutou.studio.data.local.dao.FilmToGenreDao
import ua.rikutou.studio.data.local.dao.GalleryDao
import ua.rikutou.studio.data.local.dao.GenreDao
import ua.rikutou.studio.data.local.dao.LocationDao
import ua.rikutou.studio.data.local.dao.LocationSelectionDao
import ua.rikutou.studio.data.local.dao.LocationToGalleryDao
import ua.rikutou.studio.data.local.dao.PhoneDao
import ua.rikutou.studio.data.local.dao.SectionDao
import ua.rikutou.studio.data.local.dao.StudioDao
import ua.rikutou.studio.data.local.dao.TransportDao
import ua.rikutou.studio.data.local.dao.TransportSelectionDao
import ua.rikutou.studio.data.local.dao.UserDao
import ua.rikutou.studio.data.local.entity.ActorEntity
import ua.rikutou.studio.data.local.entity.ActorToEmailEntity
import ua.rikutou.studio.data.local.entity.ActorToFilmEntity
import ua.rikutou.studio.data.local.entity.ActorToPhoneEntity
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.DepartmentToEmailEntity
import ua.rikutou.studio.data.local.entity.DepartmentToPhoneEntity
import ua.rikutou.studio.data.local.entity.EmailEntity
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.EquipmentSelectionEntity
import ua.rikutou.studio.data.local.entity.FilmEntity
import ua.rikutou.studio.data.local.entity.FilmToGenreEntity
import ua.rikutou.studio.data.local.entity.GalleryEntity
import ua.rikutou.studio.data.local.entity.GenreEntity
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.LocationSelectionEntity
import ua.rikutou.studio.data.local.entity.LocationToGalleryEntity
import ua.rikutou.studio.data.local.entity.PhoneEntity
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.local.entity.TransportSelectionEntity
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.data.remote.actor.dto.ActorToFilmDto

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
        ActorEntity::class,
        FilmEntity::class,
        ActorToFilmEntity::class,
        GenreEntity::class,
        FilmToGenreEntity::class,
        ActorToPhoneEntity::class,
        PhoneEntity::class,
        DepartmentToPhoneEntity::class,
        ActorToEmailEntity::class,
        DepartmentToEmailEntity::class,
        EmailEntity::class,
        LocationSelectionEntity::class,
        TransportSelectionEntity::class,
        EquipmentSelectionEntity::class,
    ],
    version = 20,
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
    abstract val filmDao: FilmDao
    abstract val actorToFilm: ActorToFilmDao
    abstract val genreDao: GenreDao
    abstract val filmToGenreDao: FilmToGenreDao
    abstract val actorToPhoneDao: ActorToPhoneDao
    abstract val phoneDao: PhoneDao
    abstract val departToPhoneDao: DepartmentToPhoneDao
    abstract val emailDao: EmailDao
    abstract val departmentToEmailDao: DepartmentToEmailDao
    abstract val actorToEmailDao: ActorToEmailDao
    abstract val locationSelectionDao: LocationSelectionDao
    abstract val transportSelectionDao: TransportSelectionDao
    abstract val equipmentSelectionDao: EquipmentSelectionDao
}