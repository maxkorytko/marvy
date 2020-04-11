package com.maxk.marvy.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.maxk.marvy.databinding.MarvyProgressViewBinding

class MarvyProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val layoutInflater = LayoutInflater.from(context)
        MarvyProgressViewBinding.inflate(layoutInflater, this, true)
    }
}
