package com.jaxfire.ratetracker.common

data class Change<out T>(
    val oldData: T,
    val newData: T
)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}