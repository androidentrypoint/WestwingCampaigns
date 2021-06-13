package com.example.campaigns.ui.campaigns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campaigns.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class CampaignListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val mutableCampaignFlow = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val campaignFlow = mutableCampaignFlow.flatMapLatest {
        repository.getCampaigns()
    }.shareIn(viewModelScope, SharingStarted.Lazily)

    init {
        getCampaigns()
    }

    fun getCampaigns() {
        mutableCampaignFlow.tryEmit(mutableCampaignFlow.value.not())
    }
}