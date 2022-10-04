package com.spyro.network

import com.spyro.network.interceptor.ApiRequestHeadersInterceptor
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@InstallIn(SingletonComponent::class)
@Module
internal object OkHttpModule {
    @Provides
    @Reusable
    internal fun provideOkHttpClient(
        apiRequestHeadersInterceptor: ApiRequestHeadersInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiRequestHeadersInterceptor)
        .enableLoggingIfNeeded()
        .build()

    private fun OkHttpClient.Builder.enableLoggingIfNeeded(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }
        return this
    }
}
