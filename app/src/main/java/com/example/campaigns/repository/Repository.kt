package com.example.campaigns.repository

import com.example.campaigns.model.Campaign
import com.example.campaigns.util.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getCampaigns(): Flow<NetworkStatus<List<Campaign>>>

    fun getCachedCampaigns(): Flow<List<Campaign>>
}