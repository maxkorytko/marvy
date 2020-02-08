package com.maxk.marvy.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.maxk.marvy.R
import com.maxk.marvy.model.marvel.Image

class MarvelImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView = ImageView(context, attrs, defStyleAttr)

    var image: Image? = null
        set(value) {
            field = value
            needsToSetImage = true
        }

    private var needsToSetImage: Boolean = false
        set(value) {
            field = value
            if (value) {
                requestLayout()
            }
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
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        addView(imageView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (needsToSetImage) {
            setImage(w, h)
        }
    }

    private fun setImage(width: Int, height: Int) {
        image?.run {
            val imageUrl = url(width, height)
            val thumbnailUrl = thumbnailUrl(width, height)

            imageUrl.let {
                var imageRequest = Glide.with(context).load(it)

                thumbnailUrl.let {
                    imageRequest = imageRequest.thumbnail(
                        Glide.with(context).load(it)
                    )
                }

                imageRequest.error(ColorDrawable(context.getColor(R.color.colorSurface)))
                    .into(imageView)
            }
        }
    }
}