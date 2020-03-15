package com.maxk.marvy.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.maxk.marvy.R
import com.maxk.marvy.model.marvel.Image

class MarvelImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView = ImageView(context, attrs, defStyleAttr)

    var image: Image? = null
        set(value) {
            field = value
            setImage(width, height)
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
        if (width == 0 || height == 0 || image == null) {
            Glide.with(context).clear(imageView)
        }

        image?.run {
            val imageUrl = url(width, height)
            val thumbnailUrl = thumbnailUrl(width, height)

            imageUrl.let {
                var imageRequest = Glide.with(context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

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