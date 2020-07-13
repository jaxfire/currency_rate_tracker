package com.jaxfire.ratetracker.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaxfire.ratetracker.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), RatesListAdapter.ItemClickListener {

    private val myViewModel: RateTrackerViewModel by viewModel()

    private var myAdapter: RatesListAdapter? = null

    private var hasOrderChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.rates.observe(this, Observer {
            if (it != null) {
                (recyclerView.adapter as RatesListAdapter).updateData(it, hasOrderChanged)
                hasOrderChanged = false
            }
        })

        buttonAmount.setOnClickListener {
            myViewModel.setAmount(editTextNumber.text.toString())
        }

        buttonCurrency.setOnClickListener {
            myViewModel.selectedCurrency = "GBP"
        }

        myAdapter = RatesListAdapter(mutableListOf()).apply { mySetClickListener(this@MainActivity) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = myAdapter
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

    override fun onItemClick(view: View?, position: Int) {
        myViewModel.moveItemToTop(position)
        hasOrderChanged = true
    }
}