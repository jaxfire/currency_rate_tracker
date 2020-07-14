package com.jaxfire.ratetracker.data.repository

import android.util.Log
import com.jaxfire.ratetracker.Rates
import com.jaxfire.ratetracker.data.RatesResponse
import com.jaxfire.ratetracker.data.network.RateTrackerNetworkDataSource
import io.reactivex.Observable

class RateTrackerRepositoryImpl(
    private val rateTrackerNetworkDataSource: RateTrackerNetworkDataSource
) : RateTrackerRepository {

//    private val fakeRateData = listOf(
//        Rate("USD", "US Dollar", "1.129", "imgUrl"),
//        Rate("EUR", "Euro", "1", "imgUrl"),
//        Rate("GBP", "British Pound", "0.879", "imgUrl")
//    )

    override fun getRates(currency: String): Observable<Rates> {
//        Log.d("jim", "getRates on Thread: ${Thread.currentThread()}")
        return rateTrackerNetworkDataSource.fetchRates(currency).toObservable().map { toRates(it) }
    }

    private fun toRates(response: RatesResponse): Rates? {

        var baseCurrency = ""
        val rates = HashMap<String, Double>()

        try {
            checkProperty(response.baseCurrency)
            baseCurrency = response.baseCurrency

            for (rate in response.rates) {
                try {
                    toIndividualRate(rate)
                    rates[rate.key] = rate.value
                } catch (e: IllegalStateException) {
                    reportMissingData(
                        "A null value was received for Key: ${rate.key}, Value: ${rate.value}"
                    )
                }
            }

        } catch (e: IllegalStateException) {
            reportMissingData(
                "A null value was received for the baseCurrency"
            )
        }

        return Rates(baseCurrency, rates)
    }

    private fun toIndividualRate(rate: Map.Entry<String, Double>): Pair<String, Double> {
        return with(rate) {
            checkProperty(this.key)
            checkProperty(this.value)
            Pair(this.key, this.value)
        }
    }

    private fun <T : Any> checkProperty(value: T?): T =
        checkNotNull(value)

    private fun reportMissingData(message: String) {
        // TODO: Send to cloud logging system
        Log.e(
            RateTrackerRepositoryImpl::class.java.name,
            "MissingData: getRates()_toRates(): $message"
        )
    }
}