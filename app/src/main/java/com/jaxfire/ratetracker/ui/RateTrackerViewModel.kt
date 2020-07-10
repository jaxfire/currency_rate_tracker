package com.jaxfire.ratetracker.ui

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaxfire.ratetracker.CurrencyUtil
import com.jaxfire.ratetracker.Rates
import com.jaxfire.ratetracker.data.repository.RateTrackerRepository
import io.reactivex.Observable
import io.reactivex.Observer
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

    // TODO: Decision - Make currency an observable and trigger api call on change?
    var selectedCurrency = "USD"
    private var disposable: Disposable? = null

    private val _rates = MutableLiveData<List<RateListItem>>()
    val rates: LiveData<List<RateListItem>>
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

    fun stopFetchingRates() {
        disposable?.dispose()
    }

    fun startFetchingRates() {
        Log.d("jim", "START")
        Observable.combineLatest(
            amountObservable, apiObservable,
            BiFunction { amount: Double, rates: Rates ->
                _rates.postValue(toRateListItems(amount, rates))
                return@BiFunction
            }
        )
            .subscribe(object : Observer<Any> {
                override fun onComplete() {
                    // no-op
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Any) {
                    Log.d("jim", "Tick")
                }

                override fun onError(e: Throwable) {
                    Log.d("jim", "onError: ${e.message}")
                }
            })

        // TODO: Get the actual amount figure
        setAmount("1.0")
    }


    private fun toRateListItems(amount: Double, rates: Rates): List<RateListItem> {
        // TODO: After putting API calls on IO thread ensure this runs on the computational thread
        Log.d("jim", "combine on Thread: ${Thread.currentThread()}")
        Log.d("jim", "combine amount: $amount")
        Log.d("jim", "combine rates: ${rates.baseCurrency}")

        return rates.rates.map { (key, value) ->
            RateListItem(
                key,
                getDisplayName(key),
                // TODO: Use BigDecimal instead?
                formatForUI(calculateExchangeValue(value, amount)),
                "imgUrl"
            )
        }
    }

    private fun getDisplayName(isoCode: String): String {
        val displayName = CurrencyUtil.getDisplayName(isoCode)
        return if (TextUtils.isEmpty(displayName)) isoCode else displayName!! // isEmpty checks null
    }

    private fun calculateExchangeValue(value: Double, amount: Double) =
        value * amount

    private fun formatForUI(value: Double) =
        DecimalFormat("0.00").format(value)

    fun setAmount(inputText: String) {
        if (!TextUtils.isEmpty(inputText)) {
            // TODO: BigDecimal instead?
            val inputDouble = inputText.toDoubleOrNull()
            if (inputDouble != null) {
                // Notify the Observable that the value just change
                amountObservable.onNext(inputDouble)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopFetchingRates()
    }
}