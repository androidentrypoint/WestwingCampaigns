package com.example.campaigns.ui.campaigns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campaigns.databinding.ItemCampaignLayoutBinding
import com.example.campaigns.model.Campaign

class CampaignAdapter(private val listener: Listener) :
    ListAdapter<Campaign, CampaignAdapter.ViewHolder>(Campaign.DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCampaignLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCampaignLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onItemClicked(
                    binding.root,
                    bindingAdapterPosition,
                    getItem(bindingAdapterPosition)
                )
            }
        }

        fun bind(campaign: Campaign) {
            with(campaign) {
                binding.name.text = name
                Glide.with(binding.campaignImg).load(image)
                    .into(binding.campaignImg)
            }

        }
    }

    interface Listener {
        fun onItemClicked(view: View, adapterPosition: Int, campaign: Campaign)
    }
}