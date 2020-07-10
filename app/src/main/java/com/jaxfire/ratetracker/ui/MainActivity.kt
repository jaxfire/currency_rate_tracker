package com.jaxfire.ratetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaxfire.ratetracker.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val myViewModel: RateTrackerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.rates.observe(this, Observer {
            (recyclerView.adapter as RatesListAdapter).updateData(it)
        })

        buttonAmount.setOnClickListener {
            myViewModel.setAmount(editTextNumber.text.toString())
        }

        buttonCurrency.setOnClickListener {
            myViewModel.selectedCurrency = "GBP"
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = RatesListAdapter(mutableListOf())
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
}