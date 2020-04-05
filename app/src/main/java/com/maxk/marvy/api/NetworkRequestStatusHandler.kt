package com.maxk.marvy.api

import android.view.View
import androidx.core.view.isVisible

class NetworkRequestStatusHandler(private val contentView: View?,
                                  private val errorView: View? = null,
                                  private val progressView: View? = null) {

    fun <T>handle(status: NetworkRequestStatus<T>) {
        when (status) {
            is Loading -> handleLoading()
            is Complete -> handle(status.result)
        }
    }

    private fun handleLoading() {
        progressView?.isVisible = true
        errorView?.isVisible = false
    }

    private fun <T>handle(result: Result<T>) {
        progressView?.isVisible = false

        result.fold(
            onSuccess = {
                contentView?.isVisible = true
                errorView?.isVisible = false
            },
            onFailure = {
                contentView?.isVisible = false
                errorView?.isVisible = true
            }
        )
    }
}