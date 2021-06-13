package com.example.campaigns.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.campaigns.room.entity.CampaignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {

    @Query("SELECT * FROM CampaignEntity WHERE COALESCE(name, '') != '' AND COALESCE(description, '') != ''")
    fun getFilteredCampaignsAsFlow(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM CampaignEntity")
    fun getAllCampaignsAsFlow(): Flow<List<CampaignEntity>>

    @Insert
    suspend fun insertCampaigns(campaigns: List<CampaignEntity>)

    @Query("DELETE FROM CampaignEntity")
    suspend fun deleteAllCampaigns()

}