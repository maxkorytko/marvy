package com.maxk.marvy.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.maxk.marvy.model.marvel.Image

class MarvelImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView = UrlImageView(context, attrs, defStyleAttr)

    var image: Image? = null
        set(value) {
            field = value
            setImage(width, height)
        }

    var placeholder: Drawable? = null
        set(value) {
            field = value
            imageView.placeholder = value
        }

    init {
        setupImageView()
    }

    private fun setupImageView() {
        imageView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(imageView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (oldw != w || oldh != h) {
            setImage(w, h)
        }
    }

    private fun setImage(width: Int, height: Int) {
        imageView.imageUrl = image?.url(width, height)
        imageView.thumbnailUrl = image?.thumbnailUrl(width, height)
        imageView.fetchImage()
    }
}