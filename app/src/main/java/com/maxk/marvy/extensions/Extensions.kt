package com.maxk.marvy.extensions

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.net.Uri
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import androidx.annotation.AttrRes

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Resources.Theme.resolveAttribute(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attr, typedValue, true)
    return typedValue.resourceId
}

fun String.urlEncoded(): String {
    return Uri.encode(this)
}

fun getValueAnimator(forward: Boolean = true,
                     duration: Int,
                     interpolator: TimeInterpolator = LinearInterpolator(),
                     progressListener: (Float) -> Unit): ValueAnimator {
    val animator = if (forward) {
        ValueAnimator.ofFloat(0f, 1f)
    } else {
        ValueAnimator.ofFloat(1f, 0f)
    }

    animator.addUpdateListener { progressListener(it.animatedValue as Float) }
    animator.duration = duration.toLong()
    animator.interpolator = interpolator
    return animator
}