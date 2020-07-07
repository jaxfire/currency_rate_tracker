package com.jaxfire.ratetracker.network

import androidx.lifecycle.LiveData
import io.reactivex.Single
import com.jaxfire.ratetracker.RatesResponse
import io.reactivex.Observable

interface RateTrackerNetworkDataSource {
//    val isDownloading: LiveData<Boolean>
//    val downloadedRockets: LiveData<List<RocketEntity>>
//    val downloadedLaunches: LiveData<List<LaunchEntity>>

    fun fetchRates(currency: String): Observable<RatesResponse>

//    suspend fun fetchLaunchesForRocket(rocketId: String)
}