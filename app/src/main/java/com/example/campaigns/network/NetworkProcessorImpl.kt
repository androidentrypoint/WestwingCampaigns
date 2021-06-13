package com.example.campaigns.network

import com.example.campaigns.util.DispatcherProvider
import com.example.campaigns.util.NetworkConstants
import com.example.campaigns.util.NetworkStatus
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NetworkProcessorImpl @Inject constructor(
    val dispatcherProvider: DispatcherProvider
) : NetworkProcessor {

    @Suppress("OVERRIDE_BY_INLINE", "BlockingMethodInNonBlockingContext")
    override suspend inline fun <R> processNetworkResponse(
        crossinline block: suspend () -> Response<R>
    ): NetworkStatus<R> {
        return withContext(dispatcherProvider.io()) {
            try {
                val response = block()
                when {
                    response.isSuccessful -> {
                        return@withContext NetworkStatus.Success(response.body()!!)
                    }
                    response.code() == 401 -> {
                        return@withContext NetworkStatus.Error(
                            response.errorBody()?.string()
                                ?: NetworkConstants.UNAUTHORIZED_ERROR
                        )
                    }
                    else -> {
                        return@withContext NetworkStatus.Error(
                            response.errorBody()?.string()
                                ?: NetworkConstants.UNKNOWN_NETWORK_EXCEPTION
                        )
                    }
                }
            } catch (e: Exception) {
                return@withContext NetworkStatus.Error(
                    e.message ?: NetworkConstants.UNKNOWN_NETWORK_EXCEPTION
                )
            }
        }
    }
}