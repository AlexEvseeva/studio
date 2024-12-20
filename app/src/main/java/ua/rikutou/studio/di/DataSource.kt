@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.rikutou.studio.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ua.rikutou.studio.data.datasource.actor.ActorDataSource
import ua.rikutou.studio.data.datasource.actor.ActorDataSourceImpl
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.DbDataSourceImpl
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.data.datasource.auth.AuthDataSourceImpl
import ua.rikutou.studio.data.datasource.department.DepartmentDataSource
import ua.rikutou.studio.data.datasource.department.DepartmentDataSourceImpl
import ua.rikutou.studio.data.datasource.document.DocumentDataSource
import ua.rikutou.studio.data.datasource.document.DocumentDataSourceImpl
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSource
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSourceImpl
import ua.rikutou.studio.data.datasource.execute.ExecuteDataSource
import ua.rikutou.studio.data.datasource.execute.ExecuteDataSourceImpl
import ua.rikutou.studio.data.datasource.film.FilmDataSource
import ua.rikutou.studio.data.datasource.film.FilmDataSourceImpl
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.location.LocationDataSourceImpl
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.datasource.studio.StudioDataSourceImpl
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.token.TokenDataSourceImpl
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSourceImpl
import ua.rikutou.studio.data.datasource.section.SectionDataSource
import ua.rikutou.studio.data.datasource.section.SectionDataSourceImpl
import ua.rikutou.studio.data.datasource.statistic.StatisticDataSource
import ua.rikutou.studio.data.datasource.statistic.StatisticDataSourceImpl
import ua.rikutou.studio.data.datasource.transport.TransportDataSource
import ua.rikutou.studio.data.datasource.transport.TransportDataSourceImpl
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSourceImpl
import ua.rikutou.studio.data.remote.actor.ActorApi
import ua.rikutou.studio.data.remote.department.DepartmentApi
import ua.rikutou.studio.data.remote.document.DocumentApi
import ua.rikutou.studio.data.remote.equipment.EquipmentApi
import ua.rikutou.studio.data.remote.execute.ExecuteApi
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.section.SectionApi
import ua.rikutou.studio.data.remote.statistic.StatisticApi
import ua.rikutou.studio.data.remote.studio.StudioApi
import ua.rikutou.studio.data.remote.transport.TransportApi
import ua.rikutou.studio.data.remote.user.UserApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSource {
    @Provides
    @Singleton
    fun provideTokenDataSource(
        sharedPreferences: SharedPreferences
    ) : TokenDataSource = TokenDataSourceImpl(
        sharedPreferences = sharedPreferences
    )

    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        profileDataSource: ProfileDataSource,
        tokenDataSource: TokenDataSource,
    ) : AuthDataSource = AuthDataSourceImpl(
        authApi = authApi,
        profileDataSource = profileDataSource,
        tokenDataSource = tokenDataSource,
    )

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileDataSource = ProfileDataSourceImpl()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideUserDataSource(
        userApi: UserApi,
        dbDataSource: DbDataSource,
        tokenDataSource: TokenDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): UserDataSource = UserDataSourceImpl(
        userApi = userApi,
        dbDataSource = dbDataSource,
        tokenDataSource = tokenDataSource,
        dbDeliveryDispatcher= dbDeliveryDispatcher
    )

    @Provides
    @Singleton
    fun provideDbDataSource(
        @ApplicationContext context: Context,
        userRepository: ProfileDataSource
    ): DbDataSource = DbDataSourceImpl(
        context = context,
        userRepository = userRepository
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideStudioDataSource(
        studioApi: StudioApi,
        dbDataSource: DbDataSource,
        profileDataSource: ProfileDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): StudioDataSource = StudioDataSourceImpl(
        studioApi = studioApi,
        dbDataSource = dbDataSource,
        profileDataSource = profileDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideLocationDataSource(
        locationApi: LocationApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): LocationDataSource = LocationDataSourceImpl(
        locationApi = locationApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideExecuteDataSource(
        executeApi: ExecuteApi
    ): ExecuteDataSource = ExecuteDataSourceImpl(
        executeApi = executeApi
    )

    @Provides
    fun provideDepartmentDataSource(
        departmentApi: DepartmentApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): DepartmentDataSource = DepartmentDataSourceImpl(
        departmentApi = departmentApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideSectionDataSource(
        sectionApi: SectionApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): SectionDataSource = SectionDataSourceImpl(
        sectionApi = sectionApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideEquipmentDataSource(
        equipmentApi: EquipmentApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): EquipmentDataSource = EquipmentDataSourceImpl(
        equipmentApi = equipmentApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideTransportDataSource(
        transportApi: TransportApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): TransportDataSource = TransportDataSourceImpl(
        transportApi = transportApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideActorDataSource(
        actorApi: ActorApi,
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): ActorDataSource = ActorDataSourceImpl(
        actorApi = actorApi,
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideFilmDataSource(
        dbDataSource: DbDataSource,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): FilmDataSource = FilmDataSourceImpl(
        dbDataSource = dbDataSource,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )

    @Provides
    fun provideDocumentDataSource(
        documentApi: DocumentApi
    ): DocumentDataSource = DocumentDataSourceImpl(
        documentApi = documentApi
    )

    @Provides
    fun provideStatisticDataSource(
        statisticApi: StatisticApi
    ): StatisticDataSource = StatisticDataSourceImpl(
        statisticApi = statisticApi
    )
}