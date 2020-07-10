package com.jaxfire.ratetracker

import android.icu.util.Currency

class CurrencyUtil {
    companion object {
        private val currencies: MutableMap<String, Currency> = HashMap()

        init {
            for (currency in Currency.getAvailableCurrencies()) {
                currencies[currency.currencyCode] = currency
            }
        }

        /**
         * Returns the full display name of the provided ISO 4217 code or null if the code does not
         * represent a Currency within Currency.getAvailableCurrencies(). The returned String will
         * be localised to the locale of the user's device.
         */
        fun getDisplayName(isoCode: String): String? {
            return currencies[isoCode]?.displayName
        }
    }
}