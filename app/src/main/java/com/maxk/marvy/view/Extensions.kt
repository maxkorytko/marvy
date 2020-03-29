package com.maxk.marvy.view

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Resources.Theme.resolveAttribute(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun View.showKeyboard() {
    if (requestFocus()) {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}
