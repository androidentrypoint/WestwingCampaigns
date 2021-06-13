package com.example.campaigns.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CampaignEntity(
    val name: String?,
    val description: String?,
    val urlKey: String?,
    val image: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
