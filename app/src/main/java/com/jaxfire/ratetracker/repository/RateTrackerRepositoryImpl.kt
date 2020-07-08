package com.jaxfire.ratetracker.repository

import android.util.Log
import com.jaxfire.ratetracker.Currency
import com.jaxfire.ratetracker.Rate
import com.jaxfire.ratetracker.RatesResponse
import com.jaxfire.ratetracker.network.RateTrackerNetworkDataSource
import io.reactivex.Observable

class RateTrackerRepositoryImpl(
    private val rateTrackerNetworkDataSource: RateTrackerNetworkDataSource
) : RateTrackerRepository {

    private val fakeRateData = listOf(
        Rate("USD", "US Dollar", "1.129", "imgUrl"),
        Rate("EUR", "Euro", "1", "imgUrl"),
        Rate("GBP", "British Pound", "0.879", "imgUrl")
    )

    override fun getRates(currency: Currency): Observable<RatesResponse> {
        Log.d("jim", "getRates on Thread: ${Thread.currentThread()}")
        return rateTrackerNetworkDataSource.fetchRates(currency.name)
    }
}

