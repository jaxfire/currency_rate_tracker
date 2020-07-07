package com.jaxfire.ratetracker.network

import androidx.lifecycle.LiveData
import io.reactivex.Single
import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

interface RateTrackerNetworkDataSource {
    fun fetchRates(currency: String): Observable<RatesResponse>
}