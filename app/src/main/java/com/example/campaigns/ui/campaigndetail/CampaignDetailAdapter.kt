package com.example.campaigns.ui.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campaigns.databinding.ItemCampaignDetailLayoutBinding
import com.example.campaigns.model.Campaign

class CampaignDetailAdapter :
    ListAdapter<Campaign, CampaignDetailAdapter.ViewHolder>(Campaign.DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCampaignDetailLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCampaignDetailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Campaign) {
            with(binding) {
                name.text = item.name
                description.text = item.description
                Glide.with(image).load(item.image)
                    .into(image)
            }
        }
    }
}