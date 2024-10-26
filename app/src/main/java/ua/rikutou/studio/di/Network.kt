package ua.rikutou.studio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.studio.StudioApi

@InstallIn(SingletonComponent::class)
@Module
object Network {
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideStudioApi(retrofit: Retrofit): StudioApi = retrofit.create(StudioApi::class.java)

    @Provides
    fun provideLocationApi(retrofit: Retrofit): LocationApi = retrofit.create(LocationApi::class.java)
}