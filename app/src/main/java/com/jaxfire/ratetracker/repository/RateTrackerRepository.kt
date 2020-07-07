package com.jaxfire.ratetracker.repository

import com.jaxfire.ratetracker.Currency
import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

interface RateTrackerRepository {
    fun getRates(currency: Currency): Observable<RatesResponse>
}