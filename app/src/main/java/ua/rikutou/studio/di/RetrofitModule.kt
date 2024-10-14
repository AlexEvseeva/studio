package ua.rikutou.studio.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.rikutou.studio.BuildConfig
import ua.rikutou.studio.data.repository.token.TokenDataSource
import ua.rikutou.studio.extention.addBearer
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    const val HEADER_AUTHORISATION = "Authorization"
    const val CONTENT_TYPE_KEY = "Content-Type"
    const val APPLICATION_JSON = "application/json"
    const val CONTENT_LANGUAGE_KEY = "Accept-Language"
    private val HEADERS = mapOf(
        "accept" to APPLICATION_JSON,
    )
    const val CONNECT_TIMEOUT = 10_000L
    const val READ_TIMEOUT = 10_000L
    const val CALL_TIMEOUT = 10_000L


    @Provides
    @Singleton
    fun provideRetrofitInstance (
        @ApplicationContext context: Context,
        tokenDataSource: TokenDataSource,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(BuildConfig.CFG_SERVER_URL)
            client(
                OkHttpClient.Builder().apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(getChuckerInterceptor(context))
                    }
                    addInterceptor { chain -> addHeaders(chain, tokenDataSource) }
                    connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
                }.build()
            )
            addConverterFactory(GsonConverterFactory.create(gson))
        }.build()

    private fun getChuckerInterceptor(context: Context) =
        ChuckerInterceptor.Builder(context = context)
            .collector(
                collector = ChuckerCollector(
                    context = context,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_WEEK,
                ),
            )
            .alwaysReadResponseBody(enable = true)
            .redactHeaders(HEADER_AUTHORISATION)
            .build()

    private fun addHeaders(
        chain: Interceptor.Chain,
        tokenDataSource: TokenDataSource,
    ) = chain.proceed(
        chain.request().newBuilder().apply {
            if ((chain.request().body?.contentLength() ?: 0L) > 0L) {
                addHeader(CONTENT_TYPE_KEY, APPLICATION_JSON)
            }

            HEADERS.keys.forEach { key ->
                addHeader(key, HEADERS[key].orEmpty())
            }

            addHeader(CONTENT_LANGUAGE_KEY, Locale.getDefault().language)

            tokenDataSource.token
                .takeIf { it?.isNotBlank() == true }
                ?.run { addHeader(HEADER_AUTHORISATION, addBearer()) }
        }.build(),
    )
}