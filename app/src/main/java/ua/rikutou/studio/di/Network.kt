package ua.rikutou.studio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.rikutou.studio.data.remote.actor.ActorApi
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.remote.department.DepartmentApi
import ua.rikutou.studio.data.remote.equipment.EquipmentApi
import ua.rikutou.studio.data.remote.execute.ExecuteApi
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.section.SectionApi
import ua.rikutou.studio.data.remote.studio.StudioApi
import ua.rikutou.studio.data.remote.transport.TransportApi
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

    @Provides
    fun provideSectionApi(retrofit: Retrofit): SectionApi = retrofit.create(SectionApi::class.java)

    @Provides
    fun providesEquipmentApi(retrofit: Retrofit): EquipmentApi = retrofit.create(EquipmentApi::class.java)

    @Provides
    fun provideTransportApi(retrofit: Retrofit): TransportApi = retrofit.create(TransportApi::class.java)

    @Provides
    fun provideActorApi(retrofit: Retrofit): ActorApi = retrofit.create(ActorApi::class.java)
}