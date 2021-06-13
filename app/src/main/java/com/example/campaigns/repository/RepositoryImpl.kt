package com.example.campaigns.repository

import com.example.campaigns.model.Campaign
import com.example.campaigns.network.CampaignService
import com.example.campaigns.network.NetworkProcessor
import com.example.campaigns.network.model.CampaignDTO
import com.example.campaigns.room.dao.CampaignDao
import com.example.campaigns.room.entity.CampaignEntity
import com.example.campaigns.util.DispatcherProvider
import com.example.campaigns.util.NetworkStatus
import com.example.campaigns.util.mapper.Mapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val campaignService: CampaignService,
    private val campaignDao: CampaignDao,
    private val campaignDTOMapper: Mapper<CampaignDTO, CampaignEntity>,
    private val campaignEntityMapper: Mapper<CampaignEntity, Campaign>,
    private val dispatcherProvider: DispatcherProvider,
    private val networkProcessor: NetworkProcessor
) : Repository, NetworkProcessor by networkProcessor,
    DispatcherProvider by dispatcherProvider {


    override fun getCampaigns(): Flow<NetworkStatus<List<Campaign>>> {
        return flow {
            emit(
                NetworkStatus.Loading(
                    campaignEntityMapper.mapList(
                        campaignDao.getFilteredCampaignsAsFlow().first()
                    )
                )
            )
            when (val response = processNetworkResponse { campaignService.getCampaigns() }) {
                is NetworkStatus.Success -> {
                    campaignDao.deleteAllCampaigns()
                    campaignDao.insertCampaigns(campaignDTOMapper.mapList(response.data.metadata.data))
                    emitAll(
                        campaignDao.getFilteredCampaignsAsFlow()
                            .map { NetworkStatus.Success(campaignEntityMapper.mapList(it)) })
                }
                is NetworkStatus.Error -> {
                    emit(
                        NetworkStatus.Error(
                            response.message,
                            campaignEntityMapper.mapList(
                                campaignDao.getFilteredCampaignsAsFlow().first()
                            )
                        )
                    )
                }
            }
        }.flowOn(io())
    }

    override fun getCachedCampaigns(): Flow<List<Campaign>> {
        return campaignDao.getFilteredCampaignsAsFlow().map(campaignEntityMapper::mapList)
    }


}