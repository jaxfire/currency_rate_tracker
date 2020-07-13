package com.jaxfire.ratetracker.ui

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

class RateListItemDiffCallback(private val oldRateData: List<RateListItem>, private val newRateData: List<RateListItem>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldRateData.size
    }

    override fun getNewListSize(): Int {
        return newRateData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRateData[oldItemPosition].countryCode === newRateData[newItemPosition].countryCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRateData[oldItemPosition] == newRateData[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}