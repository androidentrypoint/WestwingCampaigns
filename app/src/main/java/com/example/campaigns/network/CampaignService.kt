package com.example.campaigns.network

import com.example.campaigns.network.model.CampaignResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CampaignService {

    @Headers("Accept: application/json")
    @GET("cms/test/campaigns.json")
    suspend fun getCampaigns(): Response<CampaignResponse>
}