package com.jaxfire.ratetracker

import android.app.Application
import com.jaxfire.ratetracker.network.RateTrackerApiService
import com.jaxfire.ratetracker.network.RateTrackerNetworkDataSource
import com.jaxfire.ratetracker.network.RateTrackerNetworkDataSourceImpl
import com.jaxfire.ratetracker.repository.RateTrackerRepository
import com.jaxfire.ratetracker.repository.RateTrackerRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class RateTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@RateTrackerApplication)
            // declare modules
            modules(myModule)
        }
    }

    // TODO: Rename and move to another directory
    private val myModule = module {
//        single { ConnectivityInterceptorImpl(get()) }
        single { RateTrackerApiService() }
        single<RateTrackerNetworkDataSource> { RateTrackerNetworkDataSourceImpl(get()) }
        single<RateTrackerRepository> { RateTrackerRepositoryImpl(get())}
        viewModel { RateTrackerViewModel(get())}
    }
}