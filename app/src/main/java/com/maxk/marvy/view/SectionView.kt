package com.maxk.marvy.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.maxk.marvy.R
import com.maxk.marvy.databinding.SectionViewBinding

class SectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: SectionViewBinding

    private val contentWrapper: ViewGroup?

    var title: String? = null
        set(value) {
            field = value
            binding.contentTitle.text = value
            binding.contentTitle.visible = value?.isEmpty() == false
            binding.separator.visible = value?.isEmpty() == false
        }

    @ColorInt var color: Int = resources.getColor(R.color.colorSurface, context.theme)
        set(value) {
            field = value
            binding.container.setCardBackgroundColor(value)
        }

    fun <T>content(body: T.() -> Unit) where T: View {
        @Suppress("UNCHECKED_CAST")
        (contentWrapper?.getChildAt(0) as? T)?.let { body(it) }
    }

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = SectionViewBinding.inflate(layoutInflater, this, true)
        contentWrapper = binding.contentWrapper

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SectionView,
            0,
            0
        ).apply {
            try {
                title = getString(R.styleable.SectionView_title)
                color = getColor(
                    R.styleable.SectionView_color,
                    resources.getColor(R.color.colorSurface, context.theme)
                )
            } finally {
                recycle()
            }
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (contentWrapper == null) {
            super.addView(child, index, params)
        } else {
            contentWrapper.addView(child, index, params)
        }
    }
}