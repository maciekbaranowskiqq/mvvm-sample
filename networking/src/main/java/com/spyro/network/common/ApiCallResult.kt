package com.spyro.network.common

sealed class ApiCallResult<T> {
    data class Success<T>(val body: T) : ApiCallResult<T>()
    data class Failure<T>(
        val cause: Throwable
    ) : ApiCallResult<T>()

    data class Cancelled<T>(
        val cause: Throwable
    ) : ApiCallResult<T>()
}
