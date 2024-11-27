package ua.rikutou.studio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.remote.department.DepartmentApi
import ua.rikutou.studio.data.remote.execute.ExecuteApi
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.studio.StudioApi
import ua.rikutou.studio.data.remote.user.UserApi

@InstallIn(SingletonComponent::class)
@Module
object Network {
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideStudioApi(retrofit: Retrofit): StudioApi = retrofit.create(StudioApi::class.java)

    @Provides
    fun provideLocationApi(retrofit: Retrofit): LocationApi = retrofit.create(LocationApi::class.java)

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi =  retrofit.create(UserApi::class.java)

    @Provides
    fun provideExecuteApi(retrofit: Retrofit): ExecuteApi = retrofit.create(ExecuteApi::class.java)

    @Provides
    fun provideDepartmentApi(retrofit: Retrofit): DepartmentApi = retrofit.create(DepartmentApi::class.java)
}