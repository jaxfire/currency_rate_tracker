package com.jaxfire.ratetracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaxfire.ratetracker.repository.RateTrackerRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit

class RateTrackerViewModel(
    rateTrackerRepository: RateTrackerRepository
) : ViewModel() {

    private var selectedAmount = "0.00"
    private var selectedCurrency = Currency.GBP

    private val _rates = MutableLiveData<List<Rate>>()
    val rates: LiveData<List<Rate>>
        get() = _rates

    private var disposable = Observable
        .interval(1000L, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .flatMap { _ ->
            rateTrackerRepository.getRates(selectedCurrency)
        }
        .doOnError { error -> Log.d("jim", "error: $error") }
        .skipWhile { it.baseCurrency != selectedCurrency.name }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe() {
            _rates.postValue(it.rates.map { (key, value) ->
                Rate(key, "$key long version", value.toString(), "imgUrl")
            })

            // TODO: Calculate rates based on selected currency

//            Log.d("jim", "BaseCurrency: ${it.baseCurrency}")
//            Log.d("jim", "selectedCurrency: $selectedCurrency")

//            Log.d("jim", "${it.rates.aUD}")
//            Log.d("jim", "Thread: ${Thread.currentThread()}")
        },
            { error -> Log.d("jim", "$error") }
        )

    fun doDispose() {
        disposable.dispose()
    }

    fun setAmount(amount: String) {
        this.selectedAmount = amount
    }

    fun setCurrency(currency: Currency) {
        this.selectedCurrency = currency
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}