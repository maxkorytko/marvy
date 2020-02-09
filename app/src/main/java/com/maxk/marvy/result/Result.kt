package com.maxk.marvy.result

class Result<T> private constructor(private val data: T? = null,
                                    private val error: Throwable? = null) {

    companion object {
        fun <T>success(data: T): Result<T> =
            Result(data, null)
        fun <T>error(error: Throwable) =
            Result<T>(null, error)
    }

    private val isSuccess: Boolean = data != null
    private val isError: Boolean = error != null

    fun success(handler: (T) -> Unit) {
        if (isSuccess) {
            handler(data!!)
        }
    }

    fun error(handler: (Throwable) -> Unit) {
        if (isError) {
            handler(error!!)
        }
    }
}