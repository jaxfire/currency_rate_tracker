package com.jaxfire.ratetracker.data.network

import com.jaxfire.ratetracker.data.RatesResponse
import io.reactivex.Single

interface RateTrackerNetworkDataSource {
    fun fetchRates(currency: String): Single<RatesResponse>
}