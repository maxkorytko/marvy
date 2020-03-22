package com.maxk.marvy.view

import android.content.res.Resources
import android.util.TypedValue
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
