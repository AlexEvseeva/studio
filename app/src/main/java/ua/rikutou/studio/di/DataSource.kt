package ua.rikutou.studio.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.repository.auth.AuthRepository
import ua.rikutou.studio.data.repository.auth.AuthRepositoryImpl
import ua.rikutou.studio.data.repository.token.TokenDataSource
import ua.rikutou.studio.data.repository.token.TokenDataSourceImpl
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
        authApi: AuthApi
    ) : AuthRepository = AuthRepositoryImpl(
        authApi = authApi
    )
}