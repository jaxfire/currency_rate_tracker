package com.jaxfire.ratetracker.ui

import android.util.Log
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
//        Log.d("jim", "areItemsTheSame: ${oldRateData[oldItemPosition].countryCode == newRateData[newItemPosition].countryCode}")
        return oldRateData[oldItemPosition].countryCode == newRateData[newItemPosition].countryCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // TODO: Check against rate here instead?
//        Log.d("jim", "${oldRateData[oldItemPosition].rate == newRateData[newItemPosition].rate}")
        Log.d("jim", "oldRate: ${oldRateData[0].rate}, newRate: ${newRateData[0].rate}")
        return oldRateData[oldItemPosition].rate == newRateData[newItemPosition].rate
//        return oldRateData[oldItemPosition] == newRateData[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Change(
            oldRateData[oldItemPosition],
            newRateData[newItemPosition])
    }
}