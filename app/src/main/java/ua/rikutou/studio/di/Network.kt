package ua.rikutou.studio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.rikutou.studio.data.remote.auth.AuthApi

@InstallIn(SingletonComponent::class)
@Module
object Network {
    @Provides
    fun proveAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}