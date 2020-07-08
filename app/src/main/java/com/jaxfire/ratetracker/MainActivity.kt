package com.jaxfire.ratetracker

import android.os.Bundle
import android.text.TextUtils
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
            // TODO: Update the UI
            Log.d("jim", it[0].rate)
//            for (rate in it) {
//                Log.d("jim", "${rate.shortName} ${rate.longName} ${rate.rate}")
//            }
        })

        buttonAmount.setOnClickListener {
            // TODO: Move input validation to ViewModel
            val inputText = editTextNumber.text.toString()
            if (!TextUtils.isEmpty(inputText)) {
                // TODO: BigDecimal instead?
                val inputDouble = inputText.toDoubleOrNull()
                if (inputDouble != null) {
                    myViewModel.setAmount(inputDouble)
                }
            }
        }

        buttonCurrency.setOnClickListener {
            myViewModel.setCurrency("GBP")
        }
    }
}