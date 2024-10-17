package ua.rikutou.studio.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val SHARED_PREFERENCES_KEY = "preferences"

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @DbDeliveryDispatcher
    @Singleton
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    fun providesDbDeliveryDispatcher(): CloseableCoroutineDispatcher = newSingleThreadContext("dbDeliveryDispatcher")
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DbDeliveryDispatcher