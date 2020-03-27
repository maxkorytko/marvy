package com.maxk.marvy.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.maxk.marvy.R
import com.maxk.marvy.databinding.TitleViewBinding
import java.util.*

class TitleView @JvmOverloads constructor(
    context: Context,attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val SWITCH_DELAY: Long = 5000
    }

    private val binding: TitleViewBinding
    private val switchTimer = Timer()

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = TitleViewBinding.inflate(layoutInflater, this, true)

        binding.title.setOnClickListener {
            with(switchTimer) {
                cancel()
                purge()
            }
            switchViews()
        }

        binding.searchView.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = context.resources.getDimension(R.dimen.cardCornerRadius)
            context.theme?.resolveAttribute(R.attr.colorPrimary)?.let { setColor(it) }
            context.theme?.resolveAttribute(R.attr.colorSurface)?.let {
                setStroke(6, it)
            }
        }

        binding.viewSwitcher.inAnimation = AnimationUtils.loadAnimation(context, R.anim.search_in)
        binding.viewSwitcher.outAnimation = AnimationUtils.loadAnimation(context, R.anim.title_out)

        switchViewsAfterDelay(SWITCH_DELAY)
    }

    private fun switchViewsAfterDelay(delay: Long) {
        val task = object : TimerTask() {
            override fun run() {
                post { switchViews() }
            }
        }
        switchTimer.schedule(task, delay)
    }

    private fun switchViews() {
        binding.viewSwitcher.showNext()
    }
}
