package com.example.campaigns.network.model

import com.google.gson.annotations.SerializedName

data class CampaignResponse(
    val success: Boolean,
    val messages: Message,
    val metadata: Metadata
)

data class Message(
    val success: List<String>
)

data class Metadata(
    val count: Int,
    val data: List<CampaignDTO>
)

data class CampaignDTO(
    val name: String?,
    val description: String?,
    @SerializedName("url_key")
    val urlKey: String?,
    val image: Image
)

data class Image(
    val url: String
)
