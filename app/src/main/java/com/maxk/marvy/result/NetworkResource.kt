package com.maxk.marvy.result

sealed class NetworkResource<T>
class Complete<T>(val result: Result<T>): NetworkResource<T>()
class Loading<T>: NetworkResource<T>()

/**
 * Calls the provided block if the receiver is a Complete instance.
 */
fun <T>NetworkResource<T>.ifComplete(block: (Result<T>) -> Unit) {
    if (this is Complete) {
        block(this.result)
    }
}