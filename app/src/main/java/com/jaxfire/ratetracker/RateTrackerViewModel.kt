package com.jaxfire.ratetracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaxfire.ratetracker.repository.RateTrackerRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class RateTrackerViewModel(
    rateTrackerRepository: RateTrackerRepository
) : ViewModel() {

    private var amountObservable = PublishSubject.create<Double>()
    private var selectedCurrency = Currency.GBP
    private val compositeDisposable = CompositeDisposable()

    private val _rates = MutableLiveData<List<Rate>>()
    val rates: LiveData<List<Rate>>
        get() = _rates

    init {
        compositeDisposable.add(Observable
            .interval(1000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .flatMap { _ ->
                rateTrackerRepository.getRates(selectedCurrency)
            }
            .doOnError { error -> Log.d("jim", "error: $error") }
            .skipWhile { it.baseCurrency != selectedCurrency.name }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // TODO: Do these conversions on a background thread
                _rates.postValue(it.rates.map { (key, value) ->
                    Rate(
                        key,
                        "$key-long",
                        // TODO: Refactor this conversion to testable methods.
                        // TODO: Use BigDecimal instead.
                        // TODO: Get the amount from the latest amountObservable value
//                    DecimalFormat("0.00").format(value * selectedAmount),
                        "TODO",
                        "imgUrl"
                    )
                })

//            Log.d("jim", "BaseCurrency: ${it.baseCurrency}")
//            Log.d("jim", "selectedCurrency: $selectedCurrency")

//            Log.d("jim", "${it.rates.aUD}")
//            Log.d("jim", "Thread: ${Thread.currentThread()}")
            },
                { error -> Log.d("jim", "$error") }
            ))

        // TODO: Make the amount change be an observable so we can combineLatest with API result
        compositeDisposable.add(amountObservable.subscribe {
            Log.d("jim", "Amount updated: $it")
        })
    }


    // Use Kotlin set syntax
    fun setAmount(newAmount: Double) {
        // notify the Observable that the value just change
        amountObservable.onNext(newAmount)
    }

    // Use Kotlin set syntax
    fun setCurrency(currency: Currency) {
        this.selectedCurrency = currency
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}