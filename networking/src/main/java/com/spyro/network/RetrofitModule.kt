package com.spyro.network

import com.spyro.network.api.BackOfficeApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
internal object RetrofitModule {

    @Provides
    @Reusable
    internal fun provideBuildConfigWrapper(): BuildConfigWrapper = BuildConfigWrapper()

    @Provides
    @Reusable
    internal fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Reusable
    internal fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Reusable
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory,
        buildConfigWrapper: BuildConfigWrapper,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(buildConfigWrapper.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Provides
    @Reusable
    internal fun provideBreweryApiService(retrofit: Retrofit): BackOfficeApiService =
        retrofit.create(BackOfficeApiService::class.java)
}
