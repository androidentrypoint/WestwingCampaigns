package com.example.campaigns.ui.campaigndetail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.campaigns.databinding.ItemCampaignDetailLayoutBinding
import com.example.campaigns.model.Campaign

class CampaignDetailAdapter(private val startTransition: () -> Unit) :
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
                image.transitionName = item.image
                Glide.with(image).load(item.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            startTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            startTransition()
                            return false
                        }

                    })
                    .into(image)
            }
        }
    }
}