package com.jaxfire.ratetracker.ui

data class RateListItem(
    val currencyCode: String,
    val countryCode: String,
    val shortName: String,
    val longName: String,
    var rate: String
)