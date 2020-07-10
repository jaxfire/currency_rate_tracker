package com.jaxfire.ratetracker

data class Rates(
    val baseCurrency: String,
    val rates: Map<String, Double>
)