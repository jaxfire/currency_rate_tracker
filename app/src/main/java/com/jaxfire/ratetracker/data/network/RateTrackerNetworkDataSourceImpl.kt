package com.jaxfire.ratetracker.data.network

import com.jaxfire.ratetracker.data.RatesResponse
import io.reactivex.Single

class RateTrackerNetworkDataSourceImpl(
    private val apiService: RateTrackerApiService
) : RateTrackerNetworkDataSource {

    override fun fetchRates(currency: String): Single<RatesResponse> {
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