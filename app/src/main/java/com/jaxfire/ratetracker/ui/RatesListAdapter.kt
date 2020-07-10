package com.jaxfire.ratetracker.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaxfire.ratetracker.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class RatesListAdapter(private val data: MutableList<RateListItem>) :
    RecyclerView.Adapter<RatesListAdapter.RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rateListItem: RateListItem = data[position]
        holder.bind(rateListItem)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(rateItems: List<RateListItem>) {
        data.clear()
        data.addAll(rateItems)
        notifyDataSetChanged()
    }

    class RateViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.rate_list_item, parent, false)) {

        private var flag = itemView.findViewById<ImageView>(R.id.flag)
        private var shortName = itemView.findViewById<TextView>(R.id.shortName)
        private var longName = itemView.findViewById<TextView>(R.id.longName)
        private var value = itemView.findViewById<EditText>(R.id.value)

        fun bind(rateListItem: RateListItem) {

            Glide.with(flag.context).load("https://www.countryflags.io/${rateListItem.countryCode}/flat/64.png")
                .placeholder(R.drawable.ic_money_24)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation((flag.width / 2.91).toInt(), (flag.width / 4.66).toInt())))
                .into(flag)
            shortName?.text = rateListItem.shortName
            longName?.text = rateListItem.longName
            value?.setText(rateListItem.rate)
        }
    }
}
