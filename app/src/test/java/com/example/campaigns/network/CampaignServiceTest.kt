package com.example.campaigns.network

import com.example.campaigns.base.BaseServiceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class CampaignServiceTest : BaseServiceTest<CampaignService>(CampaignService::class.java) {

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `verify that campaign api response is properly parsed`() = runBlocking {
        enqueueResponse("sample-campaigns-response.json")
        val response = service.getCampaigns()
        val request = mockWebServer.takeRequest()
        Assert.assertEquals("GET", request.method)
        Assert.assertEquals(response.isSuccessful, true)

        val body = response.body()!!
        Assert.assertTrue(body.success)
        Assert.assertEquals(body.metadata.count, 7)
        Assert.assertEquals(body.metadata.data.first().name, "Schlafgut Spannbettlaken")
    }
}