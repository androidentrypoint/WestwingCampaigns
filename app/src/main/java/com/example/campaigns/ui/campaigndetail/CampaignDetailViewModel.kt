package com.example.campaigns.ui.campaigndetail

import androidx.lifecycle.ViewModel
import com.example.campaigns.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CampaignDetailViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val cachedCampaignFlow = repository.getCachedCampaigns()
}