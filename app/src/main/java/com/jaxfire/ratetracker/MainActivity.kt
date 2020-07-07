package com.jaxfire.ratetracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val myViewModel : RateTrackerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.rates.observe(this, Observer {
            Log.d("jim", "Rates updated")
            for (rate in it) {
                Log.d("jim", "${rate.shortName} ${rate.longName} ${rate.rate}")
            }
        })

        buttonAmount.setOnClickListener {
        }

        buttonCurrency.setOnClickListener {
            myViewModel.setCurrency(Currency.USD)
        }
    }
}