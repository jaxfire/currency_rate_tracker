package com.jaxfire.ratetracker

data class RatesResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)