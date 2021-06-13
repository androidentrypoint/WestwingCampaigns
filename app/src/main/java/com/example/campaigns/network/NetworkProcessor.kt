package com.example.campaigns.network

import com.example.campaigns.util.NetworkStatus
import retrofit2.Response

interface NetworkProcessor {

    suspend fun <R> processNetworkResponse(block: suspend () -> Response<R>): NetworkStatus<R>
}