package com.slack.exercise.dagger

import android.content.Context
import com.slack.exercise.App
import com.slack.exercise.BuildConfig
import com.slack.exercise.MainActivity
import com.slack.exercise.dagger.AppModule.Companion.Constants.CACHE_CONTROL
import com.slack.exercise.dagger.AppModule.Companion.Constants.CACHE_DIR_NAME
import com.slack.exercise.dagger.AppModule.Companion.Constants.CACHE_SIZE
import com.slack.exercise.utils.Utils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

/**
 * Module to setup Application scoped instances that require providers.
 */
@Module
abstract class AppModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @Binds
    abstract fun applicationContext(app: App): Context

    companion object {
        @Provides
        @Singleton
        fun retrofitProvider(client: OkHttpClient) =
            Retrofit.Builder()
                .baseUrl(BuildConfig.SLACK_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        @Provides
        @Singleton
        internal fun ioScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

        @Provides
        @Singleton
        fun okhttpProvider(
            context: Context,
            @Named("CachingInterceptor") cacheInterceptor: Interceptor
        ) =
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .cache(
                    Cache(
                        directory = File(context.cacheDir, CACHE_DIR_NAME),
                        maxSize = CACHE_SIZE
                    )
                )
                .addNetworkInterceptor(cacheInterceptor)
                .build()

        @Provides
        @Singleton
        @Named("CachingInterceptor")
        fun cacheInterceptorProvider(context: Context) = object :
            Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())
                if (Utils.isInternetAvailable(context)) {
                    val cacheDuration = 15.minutes.inWholeSeconds // TODO: Move to build config
                    return response.newBuilder().header(
                        CACHE_CONTROL,
                        "public, max-age=$cacheDuration"
                    ).removeHeader("pragma")
                        .build()
                } else {
                    val maxStale = 60.minutes.inWholeSeconds // TODO: Move to build config
                    return response.newBuilder()
                        .header(CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
            }
        }

        object Constants {
            const val CACHE_CONTROL = "Cache-Control"

            const val CACHE_DIR_NAME = "okhttp_cache"

            const val CACHE_SIZE = 10 * 1024 * 1024L
        }
    }
}
