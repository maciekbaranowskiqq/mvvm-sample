package com.spyro.network

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [OkHttpModule::class, RetrofitModule::class, NetworkBindingModule::class])
interface NetworkingModule
