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
import java.util.*
import java.util.concurrent.TimeUnit

class RateTrackerViewModel(
    rateTrackerRepository: RateTrackerRepository
) : ViewModel() {

    private var amountObservable = PublishSubject.create<Double>()

    // TODO: Decision - Make currency an observable and trigger api call on change?
    var selectedCurrency = "USD"
    private var disposable: Disposable? = null

//    private var theRates: List<RateListItem>? = null

    private var updateTopItem = false
//    private var itemToUpdate = 0

    private val _rates = MutableLiveData<List<RateListItem>>()
    val rates: LiveData<List<RateListItem>>
        get() = _rates

    private val apiObservable = Observable
        .interval(2_000L, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .flatMap { _ ->
            // TODO: Do this on IO thread pool
            rateTrackerRepository.getRates(selectedCurrency)
        }
        // TODO: Handle the error from a UX perspective
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

//                updateRates(toRateListItems(amount, rates))

                val rateListItems = toRateListItems(amount, rates)

                if (updateTopItem) {
                    updateTopItem = false
                    Collections.swap(rateListItems, 2, 1)
//                    for (rateItem in rateListItems) {
//                        Log.d("jim", "Code: ${rateItem.countryCode}")
//                    }
                }
//                _rates.postValue(theRates)

//                Collections.swap(rateListItems, 1, 0)
                _rates.postValue(rateListItems)
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
//                    Log.d("jim", "Tick")
                }

                override fun onError(e: Throwable) {
                    // TODO: Handle
                    Log.d("jim", "onError: ${e.message}")
                }
            })

        // TODO: Get the actual amount figure
        setAmount("1.0")
    }

//    private fun updateRates(newRates: List<RateListItem>) {
//        if (theRates == null) {
//            theRates = newRates.toList()
//        }
//        else {
//            for (oldRateItem in theRates!!) {
//                for (newRateItem in newRates) {
//                    if (oldRateItem.countryCode == newRateItem.countryCode) {
//                        oldRateItem.rate = newRateItem.rate
//                    }
//                }
//            }
//        }
//    }

    fun moveItemToTop(position: Int) {
//        itemToUpdate = position
        updateTopItem = true
    }

    private fun toRateListItems(amount: Double, rates: Rates): List<RateListItem> {
        // TODO: After putting API calls on IO thread ensure this runs on the computational thread
//        Log.d("jim", "combine on Thread: ${Thread.currentThread()}")
//        Log.d("jim", "combine amount: $amount")
//        Log.d("jim", "combine rates: ${rates.baseCurrency}")

        return rates.rates.map { (key, value) ->
            RateListItem(
                getCountryCode(key),
                key,
                getDisplayName(key),
                // TODO: Use BigDecimal instead?
                formatForUI(calculateExchangeValue(value, amount)),
                "imgUrl"
            )
        }
    }

    private fun getCountryCode(isoCode: String): String {
        return isoCode.substring(0, 2)
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