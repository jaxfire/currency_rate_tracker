package com.jaxfire.ratetracker.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jaxfire.ratetracker.R
import java.util.*


class RatesListAdapter(private var data: MutableList<RateListItem>) :
    RecyclerView.Adapter<RatesListAdapter.RateViewHolder>() {

    var mClickListener: ItemClickListener? = null

    // allows clicks events to be caught
    fun mySetClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RateViewHolder(inflater, parent, mClickListener)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rateListItem: RateListItem = data[position]
        holder.bind(rateListItem)
        holder.itemView.setOnClickListener {
            mClickListener?.onItemClick(null, position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(rateItems: List<RateListItem>, hasOrderChanged: Boolean) {

        if (hasOrderChanged) {
            Log.d("jim", "has order changed")
            val diffCallBack = RateListItemDiffCallback(data, rateItems)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            data = rateItems.toMutableList()
            diffResult.dispatchUpdatesTo(this)
        } else {
            data.clear()
            data.addAll(rateItems)
            notifyDataSetChanged()
        }
    }

    class RateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        val clickListener: ItemClickListener?
    ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.rate_list_item, parent, false)) {

        private var flag = itemView.findViewById<ImageView>(R.id.flag)
        private var shortName = itemView.findViewById<TextView>(R.id.shortName)
        private var longName = itemView.findViewById<TextView>(R.id.longName)
        private var value = itemView.findViewById<EditText>(R.id.value)

        fun bind(rateListItem: RateListItem) {
            Glide.with(flag.context)
                .load("https://www.countryflags.io/${rateListItem.countryCode}/flat/64.png")
                .placeholder(R.drawable.ic_money_24)
                .into(flag)
            shortName?.text = rateListItem.shortName
            longName?.text = rateListItem.longName
            value?.setText(rateListItem.rate)
            value?.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    clickListener?.onItemClick(null, adapterPosition)
                }
            }
        }
    }
}
