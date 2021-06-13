package com.example.campaigns.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campaigns.base.BaseDaoTest
import com.example.campaigns.network.model.CampaignResponse
import com.example.campaigns.room.entity.CampaignEntity
import com.example.campaigns.util.Util
import com.example.campaigns.util.mapper.CampaignDTOMapper
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CampaignDaoTest : BaseDaoTest() {

    @Test
    fun `verify that campaigns insertion was successful`() = runBlocking {
        val fakeCampaigns = getFakeCampaigns()
        db.campaignDao().insertCampaigns(fakeCampaigns)
        Assert.assertEquals(
            fakeCampaigns.size,
            db.campaignDao().getAllCampaignsAsFlow().first().size
        )
    }

    @Test
    fun `verify that campaigns query doesn't return campaigns without name or description`() =
        runBlocking {
            val fakeCampaigns = getFakeCampaigns()
            db.campaignDao().insertCampaigns(fakeCampaigns)
            val queriedCampaigns = db.campaignDao().getFilteredCampaignsAsFlow().first()
            Assert.assertTrue(queriedCampaigns.all {
                it.name.isNullOrEmpty().not() && it.description.isNullOrEmpty().not()
            })
        }

    private suspend fun getFakeCampaigns(): List<CampaignEntity> {
        return CampaignDTOMapper().mapList(
            Util.parseJsonFileToObject<CampaignResponse>(
                "sample-campaigns-response.json",
                object : TypeToken<CampaignResponse>() {}.type
            )?.metadata?.data.orEmpty()
        )
    }
}