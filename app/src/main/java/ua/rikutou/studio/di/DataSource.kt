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
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.DbDataSourceImpl
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.data.datasource.auth.AuthDataSourceImpl
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.location.LocationDataSourceImpl
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.datasource.studio.StudioDataSourceImpl
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.token.TokenDataSourceImpl
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSourceImpl
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSourceImpl
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.studio.StudioApi
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
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
    ): UserDataSource = UserDataSourceImpl(
        userApi = userApi,
        dbDataSource = dbDataSource,
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
}