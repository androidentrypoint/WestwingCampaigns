package com.example.campaigns.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Campaign(
    val name: String?,
    val description: String?,
    val urlKey: String?,
    val image: String,
    val id: Int
) : Parcelable {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Campaign>() {
            override fun areItemsTheSame(oldItem: Campaign, newItem: Campaign): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: Campaign, newItem: Campaign): Boolean {
                return oldItem == newItem
            }

        }
    }
}
