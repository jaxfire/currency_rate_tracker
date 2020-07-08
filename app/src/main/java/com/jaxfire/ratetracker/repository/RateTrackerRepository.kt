package com.jaxfire.ratetracker.repository

import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

interface RateTrackerRepository {
    fun getRates(currency: String): Observable<RatesResponse>
}