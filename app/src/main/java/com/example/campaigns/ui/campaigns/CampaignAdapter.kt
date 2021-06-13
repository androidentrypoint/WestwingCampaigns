package com.example.campaigns.ui.campaigns

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
                binding.campaignImg.transitionName = image
                Glide.with(binding.campaignImg).load(image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            listener.onLoadCompleted(binding.campaignImg, bindingAdapterPosition)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            listener.onLoadCompleted(binding.campaignImg, bindingAdapterPosition)
                            return false
                        }

                    })
                    .into(binding.campaignImg)
            }

        }
    }

    interface Listener {
        fun onLoadCompleted(view: View, adapterPosition: Int)

        fun onItemClicked(view: View, adapterPosition: Int, campaign: Campaign)
    }
}