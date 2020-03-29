package com.maxk.marvy.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns a LiveData that emits a value if the predicate return true.
 */
fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@filter) {
            if (predicate(it)) {
                value = it
            }
        }
    }
}