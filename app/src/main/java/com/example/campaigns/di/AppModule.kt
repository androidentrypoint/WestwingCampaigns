package com.example.campaigns.di

import android.content.Context
import com.example.campaigns.model.Campaign
import com.example.campaigns.network.CampaignService
import com.example.campaigns.network.NetworkProcessor
import com.example.campaigns.network.NetworkProcessorImpl
import com.example.campaigns.network.model.CampaignDTO
import com.example.campaigns.repository.Repository
import com.example.campaigns.repository.RepositoryImpl
import com.example.campaigns.room.AppDatabase
import com.example.campaigns.room.entity.CampaignEntity
import com.example.campaigns.util.DefaultDispatcherProvider
import com.example.campaigns.util.DispatcherProvider
import com.example.campaigns.util.mapper.CampaignDTOMapper
import com.example.campaigns.util.mapper.CampaignEntityMapper
import com.example.campaigns.util.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCampaignService(): CampaignService {
        return Retrofit.Builder()
            .baseUrl("https://static.westwing.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampaignService::class.java)
    }

    @Singleton
    @Provides
    fun provideLocalDb(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return AppDatabase.getInstance(
            context
        )
    }

    @Singleton
    @Provides
    fun provideCampaignDao(db: AppDatabase) = db.campaignDao()

}

@Module
@InstallIn(SingletonComponent::class)
interface BindingModule {

    @get:[Binds Singleton]
    val RepositoryImpl.repo: Repository

    @get:[Binds Singleton]
    val NetworkProcessorImpl.networkProcessor: NetworkProcessor

    @get:[Binds Singleton]
    val DefaultDispatcherProvider.dispatcherProvider: DispatcherProvider

    @get:[Binds Singleton]
    val CampaignDTOMapper.campaignDTOMapper: Mapper<CampaignDTO, CampaignEntity>

    @get:[Binds Singleton]
    val CampaignEntityMapper.campaignEntityMapper: Mapper<CampaignEntity, Campaign>

}