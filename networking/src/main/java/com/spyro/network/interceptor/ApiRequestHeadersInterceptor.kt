package com.spyro.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

internal class ApiRequestHeadersInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request.withPlatformHeader())
    }

    private fun Request.withPlatformHeader(): Request {
        return newBuilder()
            .header("Content-Type", "application/json")
            .build()
    }
}
