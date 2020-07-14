package com.jaxfire.ratetracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jaxfire.ratetracker.R
import kotlinx.android.synthetic.main.rate_list_item.view.*


class RatesListAdapter(private var data: MutableList<RateListItem>) :
    RecyclerView.Adapter<RatesListAdapter.RateViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(rateListItem: RateListItem, amount: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RateViewHolder(inflater, parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rateListItem: RateListItem = data[position]
        holder.bind(rateListItem)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(rateListItem, it.value.text.toString())
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newRateItems: List<RateListItem>) {

        val diffCallBack = RateListItemDiffCallback(data, newRateItems)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        diffResult.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newRateItems)
    }

    class RateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val clickListener: ItemClickListener?
    ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.rate_list_item, parent, false)) {

        private var flag = itemView.findViewById<ImageView>(R.id.flag)
        private var shortName = itemView.findViewById<TextView>(R.id.shortName)
        private var longName = itemView.findViewById<TextView>(R.id.longName)
        private var value = itemView.findViewById<EditText>(R.id.value)

        fun bind(rateListItem: RateListItem) {
            Glide.with(flag.context)
                .load("https://www.countryflags.io/${rateListItem.countryCode}/flat/64.png")
                .placeholder(R.drawable.ic_error_24)
                .into(flag)
            shortName?.text = rateListItem.shortName
            longName?.text = rateListItem.longName
            value?.setText(rateListItem.rate)
            value?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    clickListener?.onItemClick(rateListItem, value.text.toString())
                }
            }
        }
    }
}
