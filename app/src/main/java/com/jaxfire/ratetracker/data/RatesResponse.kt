package com.jaxfire.ratetracker.data

data class RatesResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)