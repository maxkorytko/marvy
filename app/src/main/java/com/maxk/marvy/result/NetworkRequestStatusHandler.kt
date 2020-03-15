package com.maxk.marvy.result

import android.view.View
import com.maxk.marvy.view.visible

class NetworkRequestStatusHandler(private val contentView: View,
                                  private val errorView: View? = null,
                                  private val progressView: View? = null) {

    fun <T>handle(status: NetworkRequestStatus<T>) {
        when (status) {
            is Loading -> progressView?.visible = true
            is Complete -> handle(status.result)
        }
    }

    private fun <T>handle(result: Result<T>) {
        progressView?.visible = false

        result.fold(
            onSuccess = {
                contentView.visible = true
                errorView?.visible = false
            },
            onFailure = {
                contentView.visible = false
                errorView?.visible = true
            }
        )
    }
}