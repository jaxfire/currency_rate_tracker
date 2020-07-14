package com.jaxfire.ratetracker.common

data class Change<out T>(
    val oldData: T,
    val newData: T
)