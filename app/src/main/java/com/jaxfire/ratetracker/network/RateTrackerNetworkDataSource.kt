package com.jaxfire.ratetracker.network

import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

interface RateTrackerNetworkDataSource {
    fun fetchRates(currency: String): Observable<RatesResponse>
}