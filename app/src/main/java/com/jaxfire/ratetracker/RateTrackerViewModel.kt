package com.jaxfire.ratetracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaxfire.ratetracker.repository.RateTrackerRepository
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class RateTrackerViewModel(
    rateTrackerRepository: RateTrackerRepository
) : ViewModel() {

    private var amountObservable = PublishSubject.create<Double>()

    // TODO: Also make this an observable and trigger api call on change?
    private var selectedCurrency = "USD"
    private val disposable: Disposable?

    private val _rates = MutableLiveData<List<Rate>>()
    val rates: LiveData<List<Rate>>
        get() = _rates

    private val apiObservable = Observable
        .interval(1000L, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .flatMap { _ ->
            // TODO: Do this on IO thread pool
            rateTrackerRepository.getRates(selectedCurrency)
        }
        .doOnError { error -> Log.d("jim", "error: $error") }
        .skipWhile { it.baseCurrency != selectedCurrency }

    init {
        disposable = Observable.combineLatest(
            amountObservable, apiObservable,
            BiFunction { amount: Double, rateResponse: RatesResponse ->

                // TODO: After putting API calls on IO thread ensure this runs on the computational thread
                Log.d("jim", "combine on Thread: ${Thread.currentThread()}")
                Log.d("jim", "combine amount: $amount")
                Log.d("jim", "combine rates: ${rateResponse.baseCurrency}")

                _rates.postValue(rateResponse.rates.map { (key, value) ->
                    Rate(
                        key,
                        "$key-long",
                        // TODO: Do these conversions on a background thread
                        // TODO: Refactor this conversion to testable methods.
                        // TODO: Use BigDecimal instead.
                        // TODO: Get the amount from the latest amountObservable value
                        DecimalFormat("0.00").format(value * amount),
//                        "TODO",
                        "imgUrl"
                    )
                })
                return@BiFunction
            })
//            .su(Schedulers.io())
            .subscribe({
                Log.d("jim", "Tick")
            }, { error -> Log.d("jim", "$error") })

        setAmount(1.0)

//            .subscribe({
//                // TODO: Do these conversions on a background thread
//                _rates.postValue(it.rates.map { (key, value) ->
//                    Rate(
//                        key,
//                        "$key-long",
//                        // TODO: Refactor this conversion to testable methods.
//                        // TODO: Use BigDecimal instead.
//                        // TODO: Get the amount from the latest amountObservable value
////                    DecimalFormat("0.00").format(value * selectedAmount),
//                        "TODO",
//                        "imgUrl"
//                    )
//                })

//            Log.d("jim", "BaseCurrency: ${it.baseCurrency}")
//            Log.d("jim", "selectedCurrency: $selectedCurrency")

//            Log.d("jim", "${it.rates.aUD}")
//            Log.d("jim", "Thread: ${Thread.currentThread()}")
//            },
//                { error -> Log.d("jim", "$error") }
//            ))

        // TODO: Make the amount change be an observable so we can combineLatest with API result
//        compositeDisposable.add(amountObservable.subscribe {
//            Log.d("jim", "Amount updated: $it")
//        })
    }


    // Use Kotlin set syntax
    fun setAmount(newAmount: Double) {
        // notify the Observable that the value just change
        amountObservable.onNext(newAmount)
    }

    // Use Kotlin set syntax
    fun setCurrency(currency: String) {
        this.selectedCurrency = currency
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}