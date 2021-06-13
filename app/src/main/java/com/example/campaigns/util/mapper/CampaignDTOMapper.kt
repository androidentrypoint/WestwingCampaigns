package com.example.campaigns.util.mapper

import com.example.campaigns.network.model.CampaignDTO
import com.example.campaigns.network.model.Image
import com.example.campaigns.room.entity.CampaignEntity
import javax.inject.Inject

class CampaignDTOMapper @Inject constructor() : Mapper<CampaignDTO, CampaignEntity> {
    override suspend fun map(from: CampaignDTO): CampaignEntity {
        return with(from) {
            CampaignEntity(name, description, urlKey, image.url)
        }
    }

    override suspend fun mapInverse(from: CampaignEntity): CampaignDTO {
        return with(from) {
            CampaignDTO(name, description, urlKey, Image(image))
        }
    }

}