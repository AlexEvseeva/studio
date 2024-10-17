package ua.rikutou.studio.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.DbDataSourceImpl
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.data.datasource.auth.AuthDataSourceImpl
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.token.TokenDataSourceImpl
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSourceImpl
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
        userDataSource: UserDataSource,
        tokenDataSource: TokenDataSource,
    ) : AuthDataSource = AuthDataSourceImpl(
        authApi = authApi,
        userDataSource = userDataSource,
        tokenDataSource = tokenDataSource
    )

    @Provides
    @Singleton
    fun provideUserRepository(): UserDataSource = UserDataSourceImpl()

    @Provides
    @Singleton
    fun provideDbDataSource(
        @ApplicationContext context: Context,
        userRepository: UserDataSource
    ): DbDataSource = DbDataSourceImpl(
        context = context,
        userRepository = userRepository
    )
}