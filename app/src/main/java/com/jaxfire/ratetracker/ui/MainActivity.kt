package com.jaxfire.ratetracker.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jaxfire.ratetracker.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private val myViewModel: RateTrackerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.rates.observe(this, Observer {
            // TODO: Update the UI
            Log.d("jim", "${it[1].shortName}, ${it[1].longName}, ${it[0].rate}")
//            for (rate in it) {
//                Log.d("jim", "${rate.shortName} ${rate.longName} ${rate.rate}")
//            }
        })

        buttonAmount.setOnClickListener {
            myViewModel.setAmount(editTextNumber.text.toString())
        }

        buttonCurrency.setOnClickListener {
            myViewModel.selectedCurrency = "GBP"
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