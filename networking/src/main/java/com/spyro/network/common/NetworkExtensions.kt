package com.spyro.network.common

import java.util.concurrent.CancellationException

suspend fun <T : Any> executeApiCallSafely(call: suspend () -> T): ApiCallResult<T> {
    return try {
        ApiCallResult.Success(body = call())
    } catch (e: CancellationException) {
        ApiCallResult.Cancelled(cause = e)
    } catch (e: Exception) {
        ApiCallResult.Failure(cause = e)
    }
}
