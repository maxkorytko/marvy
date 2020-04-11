package com.maxk.marvy.extensions

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

val View.rect: Rect get() {
    val rect = Rect()
    getDrawingRect(rect)
    return rect
}

fun View.rectInParentCoordinates(parent: ViewGroup): Rect {
    val childRect = rect
    parent.offsetDescendantRectToMyCoords(this, childRect)
    return childRect
}

fun View.showKeyboard() {
    if (requestFocus()) {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}