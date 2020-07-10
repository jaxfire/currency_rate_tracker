package com.jaxfire.ratetracker.data.repository

import com.jaxfire.ratetracker.Rates
import io.reactivex.Observable

interface RateTrackerRepository {
    fun getRates(currency: String): Observable<Rates>
}