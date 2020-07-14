package com.jaxfire.ratetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaxfire.ratetracker.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), RatesListAdapter.ItemClickListener {

    private val myViewModel: RateTrackerViewModel by viewModel()
    private var ratesAdapter: RatesListAdapter? = null

    private var hasTopItemChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.rates.observe(this, Observer {
            if (it != null) {
                (recyclerView.adapter as RatesListAdapter).updateData(it)
                if (hasTopItemChanged) {
                    hasTopItemChanged = false
                    recyclerView.scrollToPosition(0)
                }
            }
        })

        buttonAmount.setOnClickListener {
            myViewModel.setAmount(editTextNumber.text.toString())
        }

        buttonCurrency.setOnClickListener {
            myViewModel.selectedCurrency = "GBP"
        }

        ratesAdapter = RatesListAdapter(mutableListOf()).apply { setItemClickListener(this@MainActivity) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ratesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        myViewModel.startFetchingRates()
    }

    override fun onPause() {
        super.onPause()
        myViewModel.stopFetchingRates()
    }

    override fun onItemClick(currencyCode: String) {
        hasTopItemChanged = true
        myViewModel.setTopVisibleCountry(currencyCode)
    }
}