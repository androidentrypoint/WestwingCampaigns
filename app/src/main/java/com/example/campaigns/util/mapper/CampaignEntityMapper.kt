package com.example.campaigns.util.mapper

import com.example.campaigns.model.Campaign
import com.example.campaigns.room.entity.CampaignEntity
import javax.inject.Inject

class CampaignEntityMapper @Inject constructor() : Mapper<CampaignEntity, Campaign> {
    override suspend fun map(from: CampaignEntity): Campaign {
        return with(from) {
            Campaign(name, description, urlKey, image, id)
        }
    }

    override suspend fun mapInverse(from: Campaign): CampaignEntity {
        return with(from) {
            CampaignEntity(name, description, urlKey, image, id)
        }
    }

}