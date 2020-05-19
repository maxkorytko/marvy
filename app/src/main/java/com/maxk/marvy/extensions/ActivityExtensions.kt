package com.maxk.marvy.extensions

import android.app.Activity
import com.maxk.marvy.R

fun Activity.overrideEnterTransition() {
    overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit)
}

fun Activity.overrideExitTransition() {
    overridePendingTransition(R.anim.transition_exit_reverse, R.anim.transition_enter_reverse)
}
