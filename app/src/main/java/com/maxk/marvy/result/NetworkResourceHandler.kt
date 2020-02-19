package com.maxk.marvy.result

import android.view.View
import com.maxk.marvy.view.visible

class NetworkResourceHandler(private val contentView: View,
                             private val errorView: View? = null,
                             private val progressView: View? = null) {

    fun <T>handle(resource: NetworkResource<T>) {
        when (resource) {
            is Loading -> progressView?.visible = true
            is Complete -> handle(resource.result)
        }
    }

    private fun <T>handle(result: Result<T>) {
        progressView?.visible = false

        result.success {
            contentView.visible = true
            errorView?.visible = false
        }

        result.error {
            contentView.visible = false
            errorView?.visible = true
        }
    }
}