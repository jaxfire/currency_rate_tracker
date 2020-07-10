package com.jaxfire.ratetracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jaxfire.ratetracker.R


class RatesListAdapter(private val data: MutableList<RateListItem>)
    : RecyclerView.Adapter<RatesListAdapter.RateViewHolder>() {

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
        private var flag: ImageView? = null
        private var shortName: TextView? = null
        private var longName: TextView? = null
        private var value: EditText? = null

        init {
            flag = itemView.findViewById(R.id.flag)
            shortName = itemView.findViewById(R.id.shortName)
            longName = itemView.findViewById(R.id.longName)
            value = itemView.findViewById(R.id.value)
        }

        fun bind(rateListItem: RateListItem) {
//            flag?.setImageResource() =
            shortName?.text = rateListItem.shortName
            longName?.text = rateListItem.longName
            value?.setText(rateListItem.rate)
        }
    }
}
