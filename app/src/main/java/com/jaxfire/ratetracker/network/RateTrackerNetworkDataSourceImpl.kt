package com.jaxfire.ratetracker.network

import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

class RateTrackerNetworkDataSourceImpl(
    private val apiService: RateTrackerApiService
) : RateTrackerNetworkDataSource {

    override fun fetchRates(currency: String): Observable<RatesResponse> {
        return apiService.getRates(currency)

//        try {
//            val fetchedRockets = apiService.getRates(currency)
//        } catch (e: NoConnectivityException) {
//            Log.e("Connectivity", "No internet connection", e)
//            // TODO: Update the UI / notify the user. 2nd live data to display 'No internet' message
//            _downloadedRockets.postValue(emptyList())
//        }
    }
}