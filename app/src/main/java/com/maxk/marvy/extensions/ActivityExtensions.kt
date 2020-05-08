package com.maxk.marvy.extensions

import android.app.Activity
import com.maxk.marvy.R

fun Activity.overrideEnterTransition() {
    overridePendingTransition(R.anim.actvity_enter, R.anim.activity_exit)
}

fun Activity.overrideExitTransition() {
    overridePendingTransition(R.anim.activity_exit_reverse, R.anim.actvity_enter_reverse)
}
