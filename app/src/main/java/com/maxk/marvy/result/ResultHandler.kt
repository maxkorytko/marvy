package com.maxk.marvy.result

import android.view.View

class ResultHandler(private val contentView: View, private val errorView: View) {
    fun <T>handle(result: Result<T>) {
        result.success {
            contentView.visibility = View.VISIBLE
            errorView.visibility = View.GONE
        }

        result.error {
            contentView.visibility = View.GONE
            errorView.visibility = View.VISIBLE
        }
    }
}