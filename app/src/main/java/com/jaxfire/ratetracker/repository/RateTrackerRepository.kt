package com.jaxfire.ratetracker.repository

import com.jaxfire.ratetracker.Currency
import com.jaxfire.ratetracker.Rate
import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable
import io.reactivex.Single

interface RateTrackerRepository {
    fun getRates(currency: Currency): Observable<RatesResponse>
}