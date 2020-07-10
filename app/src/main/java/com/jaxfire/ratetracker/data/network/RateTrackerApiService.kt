package com.jaxfire.ratetracker.data.network

import com.jaxfire.ratetracker.data.RatesResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RateTrackerApiService {

    @GET("latest")
    fun getRates(@Query("base") base: String): Single<RatesResponse>

    companion object {
        operator fun invoke(
//            connectivityInterceptor: ConnectivityInterceptor
        ): RateTrackerApiService {

            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://hiring.revolut.codes/api/android/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RateTrackerApiService::class.java)
        }
    }
}