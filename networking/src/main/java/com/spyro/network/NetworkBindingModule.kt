package com.spyro.network

import com.spyro.network.api.BackOfficeNetwork
import com.spyro.network.api.BackOfficeNetworkImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface NetworkBindingModule {
    @Binds
    fun bindBackOfficeNetwork(backOfficeNetworkImpl: BackOfficeNetworkImpl): BackOfficeNetwork
}
