package com.jaxfire.ratetracker.ui

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaxfire.ratetracker.common.CurrencyUtil
import com.jaxfire.ratetracker.Rates
import com.jaxfire.ratetracker.data.repository.RateTrackerRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
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

    // TODO: Make an observable too
    private var topCountryCode = PublishSubject.create<String>()

    private val _rates = MutableLiveData<List<RateListItem>>()
    val rates: LiveData<List<RateListItem>>
        get() = _rates

    private val apiObservable = Observable
        .interval(1_000L, TimeUnit.MILLISECONDS)
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
            listOf(amountObservable, apiObservable, topCountryCode)) {

            val amount = it[0] as Double
            val rates = it[1] as Rates
            val topCountryCode = it[2] as String

            val rateItems = toRateListItems(amount, rates)

            if (topCountryCode.isNotEmpty()) {

                var topCountryIndex = 0
                rateItems.forEachIndexed { index, rateListItem ->
                    if (rateListItem.currencyCode == topCountryCode) {
                        topCountryIndex = index
                        return@forEachIndexed
                    }
                }
                Collections.swap(rateItems, topCountryIndex, 0)
            }

            _rates.postValue(rateItems)

        }.subscribe(object : Observer<Any> {
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
        setTopVisibleCurrency("")
    }

    fun setTopVisibleCurrency(countryCode: String) {
        topCountryCode.onNext(countryCode)
    }

    private fun toRateListItems(amount: Double, rates: Rates): List<RateListItem> {
        // TODO: After putting API calls on IO thread ensure this runs on the computational thread
//        Log.d("jim", "combine on Thread: ${Thread.currentThread()}")
//        Log.d("jim", "combine amount: $amount")
//        Log.d("jim", "combine rates: ${rates.baseCurrency}")

        return rates.rates.map { (key, value) ->
            RateListItem(
                getCountryCodeFromCurrencyCode(key),
                key,
                getDisplayName(key),
                // TODO: Use BigDecimal instead?
                formatForUI(calculateExchangeValue(value, amount)),
                "imgUrl"
            )
        }
    }

    private fun getCountryCodeFromCurrencyCode(isoCode: String): String {
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