package com.maxk.marvy.result

sealed class NetworkRequestStatus<T>(val isInitialRequest: Boolean? = null)

class Complete<T>(isInitialRequest: Boolean? = null, val result: Result<T>)
    : NetworkRequestStatus<T>(isInitialRequest)

class Loading<T>(isInitialRequest: Boolean? = null)
    : NetworkRequestStatus<T>(isInitialRequest)

