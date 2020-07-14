package com.jaxfire.ratetracker.ui

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.jaxfire.ratetracker.common.Change

class RateListItemDiffCallback(private val oldRateData: List<RateListItem>, private val newRateData: List<RateListItem>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldRateData.size
    }

    override fun getNewListSize(): Int {
        return newRateData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRateData[oldItemPosition].currencyCode == newRateData[newItemPosition].currencyCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRateData[oldItemPosition].rate == newRateData[newItemPosition].rate
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Change(
            oldRateData[oldItemPosition],
            newRateData[newItemPosition])
    }
}